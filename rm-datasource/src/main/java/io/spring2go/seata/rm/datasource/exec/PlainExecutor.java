package io.spring2go.seata.rm.datasource.exec;

import io.spring2go.seata.rm.datasource.StatementProxy;
import io.spring2go.seata.rm.datasource.sql.SQLRecognizer;

import java.sql.Statement;

/**
 * Created by william on May, 2020
 */
public class PlainExecutor<T, S extends Statement> implements Executor {
    private StatementProxy<S> statementProxy;

    private StatementCallback<T, S> statementCallback;

    private SQLRecognizer sqlRecognizer;

    public PlainExecutor(StatementProxy<S> statementProxy, StatementCallback<T, S> statementCallback, SQLRecognizer sqlRecognizer) {
        this.statementProxy = statementProxy;
        this.statementCallback = statementCallback;
        this.sqlRecognizer = sqlRecognizer;
    }

    @Override
    public T execute(Object... args) throws Throwable {
        return statementCallback.execute(statementProxy.getTargetStatement(), args);
    }
}
