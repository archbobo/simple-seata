package io.spring2go.seata.rm.datasource.undo;

import com.alibaba.druid.util.JdbcConstants;
import io.spring2go.seata.common.exception.NotSupportYetException;
import io.spring2go.seata.common.util.BlobUtils;
import io.spring2go.seata.common.util.StringUtils;
import io.spring2go.seata.core.exception.TransactionException;
import io.spring2go.seata.rm.datasource.ConnectionContext;
import io.spring2go.seata.rm.datasource.ConnectionProxy;
import io.spring2go.seata.rm.datasource.DataSourceProxy;
import io.spring2go.seata.rm.datasource.sql.struct.TableMeta;
import io.spring2go.seata.rm.datasource.sql.struct.TableMetaCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

import static io.spring2go.seata.core.exception.TransactionExceptionCode.BranchRollbackFailed_Retriable;

/**
 * Created by william on May, 2020
 */
public class UndoLogManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(UndoLogManager.class);

    private static String UNDO_LOG_TABLE_NAME = "undo_log";

    private static String INSERT_UNDO_LOG_SQL = "INSERT INTO " + UNDO_LOG_TABLE_NAME + "\n" +
            "\t(branch_id, xid, rollback_info, log_status, log_created, log_modified)\n" +
            "VALUES (?, ?, ?, 0, now(), now())";
    private static String DELETE_UNDO_LOG_SQL = "DELETE FROM " + UNDO_LOG_TABLE_NAME + "\n" +
            "\tWHERE branch_id = ? AND xid = ?";

    private static String SELECT_UNDO_LOG_SQL = "SELECT * FROM undo_log WHERE log_status = 0 AND branch_id = ? AND xid = ? FOR UPDATE";

    private UndoLogManager() {

    }

    public static void flushUndoLogs(ConnectionProxy cp) throws SQLException {
        assertDbSupport(cp.getDbType());

        ConnectionContext connectionContext = cp.getContext();
        String xid = connectionContext.getXid();
        long branchID = connectionContext.getBranchId();

        BranchUndoLog branchUndoLog = new BranchUndoLog();
        branchUndoLog.setXid(xid);
        branchUndoLog.setBranchId(branchID);
        branchUndoLog.setSqlUndoLogs(connectionContext.getUndoItems());

        String undoLogContent = UndoLogParserFactory.getInstance().encode(branchUndoLog);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Flushing UNDO LOG: " + undoLogContent);
        }

        PreparedStatement pst = null;
        try {
            pst = cp.getTargetConnection().prepareStatement(INSERT_UNDO_LOG_SQL);
            pst.setLong(1, branchID);
            pst.setString(2, xid);
            pst.setBlob(3, BlobUtils.string2blob(undoLogContent));
            pst.executeUpdate();
        } catch (Exception e) {
            if (e instanceof SQLException) {
                throw (SQLException) e;
            } else {
                throw new SQLException(e);
            }
        } finally {
            if (pst != null) {
                pst.close();
            }
        }

    }

    private static void assertDbSupport(String dbType) {
        if (!JdbcConstants.MYSQL.equals(dbType)) {
            throw new NotSupportYetException("DbType[" + dbType + "] is not support yet!");
        }
    }

    public static void undo(DataSourceProxy dataSourceProxy, String xid, long branchId) throws TransactionException {
        assertDbSupport(dataSourceProxy.getTargetDataSource().getDbType());

        Connection conn = null;
        ResultSet rs = null;
        PreparedStatement selectPST = null;
        try {
            conn = dataSourceProxy.getPlainConnection();

            // The entire undo process should run in a local transaction.
            conn.setAutoCommit(false);

            // Find UNDO LOG
            selectPST = conn.prepareStatement(SELECT_UNDO_LOG_SQL);
            selectPST.setLong(1, branchId);
            selectPST.setString(2, xid);
            rs = selectPST.executeQuery();

            while (rs.next()) {
                Blob b = rs.getBlob("rollback_info");
                String rollbackInfo = StringUtils.blob2string(b);
                BranchUndoLog branchUndoLog = UndoLogParserFactory.getInstance().decode(rollbackInfo);

                for (SQLUndoLog sqlUndoLog : branchUndoLog.getSqlUndoLogs()) {
                    TableMeta tableMeta = TableMetaCache.getTableMeta(dataSourceProxy, sqlUndoLog.getTableName());
                    sqlUndoLog.setTableMeta(tableMeta);
                    AbstractUndoExecutor undoExecutor = UndoExecutorFactory.getUndoExecutor(dataSourceProxy.getDbType(), sqlUndoLog);
                    undoExecutor.executeOn(conn);
                }

            }
            deleteUndoLog(xid, branchId, conn);

            conn.commit();

        } catch (Throwable e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    LOGGER.warn("Failed to close JDBC resource while undo ... ", rollbackEx);
                }
            }
            throw new TransactionException(BranchRollbackFailed_Retriable, String.format("%s/%s", branchId, xid), e);

        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (selectPST != null) {
                    selectPST.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException closeEx) {
                LOGGER.warn("Failed to close JDBC resource while undo ... ", closeEx);
            }
        }

    }

    public static void deleteUndoLog(String xid, long branchId, Connection conn) throws SQLException {
        PreparedStatement deletePST = conn.prepareStatement(DELETE_UNDO_LOG_SQL);
        deletePST.setLong(1, branchId);
        deletePST.setString(2, xid);
        deletePST.executeUpdate();
    }
}
