package io.spring2go.seata.rm.datasource;

import io.spring2go.seata.core.exception.TransactionException;
import io.spring2go.seata.core.exception.TransactionExceptionCode;
import io.spring2go.seata.core.model.BranchStatus;
import io.spring2go.seata.core.model.BranchType;
import io.spring2go.seata.rm.datasource.exec.LockConflictException;
import io.spring2go.seata.rm.datasource.sql.SQLType;
import io.spring2go.seata.rm.datasource.sql.struct.Field;
import io.spring2go.seata.rm.datasource.sql.struct.TableRecords;
import io.spring2go.seata.rm.datasource.undo.SQLUndoLog;
import io.spring2go.seata.rm.datasource.undo.UndoLogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by william on May, 2020
 */
public class ConnectionProxy extends AbstractConnectionProxy {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionProxy.class);

    private ConnectionContext context = new ConnectionContext();

    public ConnectionProxy(DataSourceProxy dataSourceProxy, Connection targetConnection, String dbType) {
        super(dataSourceProxy, targetConnection, dbType);
    }

    public ConnectionContext getContext() {
        return context;
    }

    public void bind(String xid) {
        context.bind(xid);
    }

    public void checkLock(TableRecords records) throws SQLException {
        // Just check lock without requiring lock by now.
        String lockKeys = buildLockKey(records);
        try {
            boolean lockable = DataSourceManager.get().lockQuery(BranchType.AT, getDataSourceProxy().getResourceId(), context.getXid(), lockKeys);
            if (!lockable) {
                throw new LockConflictException();
            }
        } catch (TransactionException e) {
            recognizeLockKeyConflictException(e);
        }
    }
    public void register(TableRecords records) throws SQLException {
        // Just check lock without requiring lock by now.
        String lockKeys = buildLockKey(records);
        try {
            DataSourceManager.get().branchRegister(BranchType.AT, getDataSourceProxy().getResourceId(), null, context.getXid(), lockKeys);
        } catch (TransactionException e) {
            recognizeLockKeyConflictException(e);
        }
    }

    private void recognizeLockKeyConflictException(TransactionException te) throws SQLException {
        if (te.getCode() == TransactionExceptionCode.LockKeyConflict) {
            throw new LockConflictException();
        } else {
            throw new SQLException(te);
        }

    }

    public void prepareUndoLog(SQLType sqlType, String tableName, TableRecords beforeImage, TableRecords afterImage) throws SQLException {
        TableRecords lockKeyRecords = afterImage;
        if (sqlType == SQLType.DELETE) {
            lockKeyRecords = beforeImage;
        }
        String lockKeys = buildLockKey(lockKeyRecords);
        context.appendLockKey(lockKeys);
        SQLUndoLog sqlUndoLog = buildUndoItem(sqlType, tableName, beforeImage, afterImage);
        context.appendUndoItem(sqlUndoLog);
    }

    private SQLUndoLog buildUndoItem(SQLType sqlType, String tableName, TableRecords beforeImage, TableRecords afterImage) {
        SQLUndoLog sqlUndoLog = new SQLUndoLog();
        sqlUndoLog.setSqlType(sqlType);
        sqlUndoLog.setTableName(tableName);
        sqlUndoLog.setBeforeImage(beforeImage);
        sqlUndoLog.setAfterImage(afterImage);
        return sqlUndoLog;
    }

    private String buildLockKey(TableRecords rowsIncludingPK) {
        if (rowsIncludingPK.size() == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(rowsIncludingPK.getTableMeta().getTableName());
        sb.append(":");

        boolean flag = false;
        for (Field field : rowsIncludingPK.pkRows()) {
            if (flag) {
                sb.append(",");
            } else {
                flag = true;
            }
            sb.append(field.getValue());
        }
        return sb.toString();
    }

    @Override
    public void commit() throws SQLException {
        if (context.inGlobalTransaction()) {
            try {
                register();
            } catch (TransactionException e) {
                recognizeLockKeyConflictException(e);
            }

            try {
                if (context.hasUndoLog()) {
                    UndoLogManager.flushUndoLogs(this);
                }
                targetConnection.commit();
            } catch (Throwable ex) {
                report(false);
                if (ex instanceof SQLException) {
                    throw (SQLException) ex;
                } else {
                    throw new SQLException(ex);
                }

            }
            report(true);
            context.reset();

        } else {
            targetConnection.commit();
        }
    }

    private void register() throws TransactionException {
        Long branchId = DataSourceManager.get().branchRegister(BranchType.AT, getDataSourceProxy().getResourceId(),
                null, context.getXid(), context.buildLockKeys());
        context.setBranchId(branchId);
    }

    @Override
    public void rollback() throws SQLException {
        targetConnection.rollback();
        if (context.inGlobalTransaction()) {
            if (context.isBranchRegistered()) {
                report(false);
            }}
        context.reset();
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        if ((autoCommit) && !getAutoCommit()) {
            // change autocommit from false to true, we should commit() first according to JDBC spec.
            commit();
        }
        targetConnection.setAutoCommit(autoCommit);
    }

    private void report(boolean commitDone) throws SQLException {
        int retry = 5; // TODO: configure
        while (retry > 0) {
            try {
                DataSourceManager.get().branchReport(context.getXid(), context.getBranchId(),
                        (commitDone ? BranchStatus.PhaseOne_Done : BranchStatus.PhaseOne_Failed), null);
                return;
            } catch (Throwable ex) {
                LOGGER.error("Failed to report [" + context.getBranchId() + "/" + context.getXid() + "] commit done [" + commitDone + "] Retry Countdown: " + retry);
                retry--;

                if (retry == 0) {
                    throw new SQLException("Failed to report branch status " + commitDone, ex);
                }
            }
        }
    }
}
