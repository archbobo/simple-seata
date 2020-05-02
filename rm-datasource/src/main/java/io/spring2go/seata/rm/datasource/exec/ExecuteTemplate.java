package io.spring2go.seata.rm.datasource.exec;

import io.spring2go.seata.core.context.RootContext;
import io.spring2go.seata.rm.datasource.StatementProxy;
import io.spring2go.seata.rm.datasource.sql.SQLRecognizer;
import io.spring2go.seata.rm.datasource.sql.SQLVisitorFactory;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by william on May, 2020
 */
public class ExecuteTemplate {
    public static <T, S extends Statement> T execute(StatementProxy<S> statementProxy,
                                                     StatementCallback<T, S> statementCallback,
                                                     Object... args) throws SQLException {
        return execute(null, statementProxy, statementCallback, args);
    }

    public static <T, S extends Statement> T execute(SQLRecognizer sqlRecognizer,
                                                     StatementProxy<S> statementProxy,
                                                     StatementCallback<T, S> statementCallback,
                                                     Object... args) throws SQLException {

        if (!RootContext.inGlobalTransaction()) {
            // Just work as original statement
            return statementCallback.execute(statementProxy.getTargetStatement(), args);
        }

        if (sqlRecognizer == null) {
            sqlRecognizer = SQLVisitorFactory.get(
                    statementProxy.getTargetSQL(),
                    statementProxy.getConnectionProxy().getDbType());
        }
        Executor<T> executor = null;
        switch (sqlRecognizer.getSQLType()) {
            case INSERT:
                executor = new InsertExecutor<T, S>(statementProxy, statementCallback, sqlRecognizer);
                break;
            case UPDATE:
                executor = new UpdateExecutor<T, S>(statementProxy, statementCallback, sqlRecognizer);
                break;
            case DELETE:
                executor = new DeleteExecutor<T, S>(statementProxy, statementCallback, sqlRecognizer);
                break;
            case SELECT_FOR_UPDATE:
                executor = new SelectForUpdateExecutor(statementProxy, statementCallback, sqlRecognizer);
                break;
            default:
                executor = new PlainExecutor<T, S>(statementProxy, statementCallback, sqlRecognizer);
                break;
        }
        T rs = null;
        try {
            rs = executor.execute(args);

        } catch (Throwable ex) {
            if (ex instanceof SQLException) {
                throw (SQLException) ex;
            } else {
                // Turn everything into SQLException
                new SQLException(ex);
            }
        }
        return rs;
    }
}
