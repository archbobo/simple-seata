package io.spring2go.seata.rm.datasource;

import io.spring2go.seata.rm.datasource.exec.ExecuteTemplate;
import io.spring2go.seata.rm.datasource.exec.StatementCallback;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by william on May, 2020
 */
public class PreparedStatementProxy extends AbstractPreparedStatementProxy implements PreparedStatement, ParametersHolder {
    @Override
    public ArrayList<Object>[] getParameters() {
        return parameters;
    }

    private void init() throws SQLException {
        int paramCount = targetStatement.getParameterMetaData().getParameterCount();
        this.parameters = new ArrayList[paramCount];
        for (int i = 0; i < paramCount; i++) {
            parameters[i] = new ArrayList<>();
        }
    }

    public PreparedStatementProxy(AbstractConnectionProxy connectionProxy, PreparedStatement targetStatement, String targetSQL) throws SQLException {
        super(connectionProxy, targetStatement, targetSQL);
        init();
    }

    @Override
    public boolean execute() throws SQLException {
        return ExecuteTemplate.execute(this, new StatementCallback<Boolean, PreparedStatement>() {
            @Override
            public Boolean execute(PreparedStatement statement, Object... args) throws SQLException {
                return statement.execute();
            }
        });
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        return ExecuteTemplate.execute(this, new StatementCallback<ResultSet, PreparedStatement>() {
            @Override
            public ResultSet execute(PreparedStatement statement, Object... args) throws SQLException {
                return statement.executeQuery();
            }
        });
    }

    @Override
    public int executeUpdate() throws SQLException {
        return ExecuteTemplate.execute(this, new StatementCallback<Integer, PreparedStatement>() {
            @Override
            public Integer execute(PreparedStatement statement, Object... args) throws SQLException {
                return statement.executeUpdate();
            }
        });
    }
}
