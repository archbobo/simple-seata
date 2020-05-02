package io.spring2go.seata.rm.datasource.exec;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by william on May, 2020
 */
public interface StatementCallback<T, S extends Statement> {
    T execute(S statement, Object... args) throws SQLException;
}
