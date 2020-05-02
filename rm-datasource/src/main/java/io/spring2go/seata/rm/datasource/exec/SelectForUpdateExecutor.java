package io.spring2go.seata.rm.datasource.exec;

import io.spring2go.seata.common.util.StringUtils;
import io.spring2go.seata.rm.datasource.ParametersHolder;
import io.spring2go.seata.rm.datasource.StatementProxy;
import io.spring2go.seata.rm.datasource.sql.SQLRecognizer;
import io.spring2go.seata.rm.datasource.sql.SQLSelectRecognizer;
import io.spring2go.seata.rm.datasource.sql.struct.TableRecords;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by william on May, 2020
 */
public class SelectForUpdateExecutor<S extends Statement> extends BaseTransactionalExecutor<ResultSet, S> {
    public SelectForUpdateExecutor(StatementProxy<S> statementProxy, StatementCallback<ResultSet, S> statementCallback, SQLRecognizer sqlRecognizer) {
        super(statementProxy, statementCallback, sqlRecognizer);
    }

    @Override
    public Object doExecute(Object... args) throws Throwable {
        SQLSelectRecognizer recognizer = (SQLSelectRecognizer) sqlRecognizer;

        Connection conn = statementProxy.getConnection();
        ResultSet rs = null;
        Savepoint sp = null;
        LockRetryController lockRetryController = new LockRetryController();
        boolean originalAutoCommit = conn.getAutoCommit();

        StringBuffer selectSQLAppender = new StringBuffer("SELECT ");
        selectSQLAppender.append(getTableMeta().getPkName());
        selectSQLAppender.append(" FROM " + getTableMeta().getTableName());
        String whereCondition = null;
        ArrayList<Object> paramAppender = new ArrayList<>();
        if (statementProxy instanceof ParametersHolder) {
            whereCondition = recognizer.getWhereCondition((ParametersHolder) statementProxy, paramAppender);
        } else {
            whereCondition = recognizer.getWhereCondition();
        }
        if (!StringUtils.isEmpty(whereCondition)) {
            selectSQLAppender.append(" WHERE " + whereCondition);
        }
        selectSQLAppender.append(" FOR UPDATE");
        String selectPKSQL = selectSQLAppender.toString();

        try {
            if (originalAutoCommit) {
                conn.setAutoCommit(false);
            }
            sp = conn.setSavepoint();
            rs = statementCallback.execute(statementProxy.getTargetStatement(), args);

            while (true) {
                // Try to get global lock of those rows selected
                Statement stPK = null;
                PreparedStatement pstPK = null;
                ResultSet rsPK = null;
                try {
                    if (paramAppender.isEmpty()) {
                        stPK = statementProxy.getConnection().createStatement();
                        rsPK = stPK.executeQuery(selectPKSQL);
                    } else {
                        pstPK = statementProxy.getConnection().prepareStatement(selectPKSQL);
                        for (int i = 0; i < paramAppender.size(); i++) {
                            pstPK.setObject(i + 1, paramAppender.get(i));
                        }
                        rsPK = pstPK.executeQuery();
                    }

                    TableRecords selectPKRows = TableRecords.buildRecords(getTableMeta(), rsPK);
                    statementProxy.getConnectionProxy().checkLock(selectPKRows);
                    break;

                } catch (LockConflictException lce) {
                    conn.rollback(sp);
                    lockRetryController.sleep(lce);

                } finally {
                    if (rsPK != null) {
                        rsPK.close();
                    }
                    if (stPK != null) {
                        stPK.close();
                    }
                    if (pstPK != null) {
                        pstPK.close();
                    }
                }
            }

        } finally {
            if (sp != null) {
                conn.releaseSavepoint(sp);
            }
            if (originalAutoCommit) {
                conn.setAutoCommit(true);
            }
        }
        return rs;
    }
}
