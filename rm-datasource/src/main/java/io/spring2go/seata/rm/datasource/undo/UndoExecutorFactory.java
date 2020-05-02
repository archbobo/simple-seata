package io.spring2go.seata.rm.datasource.undo;

import com.alibaba.druid.util.JdbcConstants;
import io.spring2go.seata.common.exception.NotSupportYetException;
import io.spring2go.seata.common.exception.ShouldNeverHappenException;
import io.spring2go.seata.rm.datasource.undo.mysql.MySQLUndoDeleteExecutor;
import io.spring2go.seata.rm.datasource.undo.mysql.MySQLUndoInsertExecutor;
import io.spring2go.seata.rm.datasource.undo.mysql.MySQLUndoUpdateExecutor;

/**
 * Created by william on May, 2020
 */
public class UndoExecutorFactory {
    public static AbstractUndoExecutor getUndoExecutor(String dbType, SQLUndoLog sqlUndoLog) {
        if (!dbType.equals(JdbcConstants.MYSQL)) {
            throw new NotSupportYetException(dbType);
        }
        switch (sqlUndoLog.getSqlType()) {
            case INSERT:
                return new MySQLUndoInsertExecutor(sqlUndoLog);
            case UPDATE:
                return new MySQLUndoUpdateExecutor(sqlUndoLog);
            case DELETE:
                return new MySQLUndoDeleteExecutor(sqlUndoLog);
            default:
                throw new ShouldNeverHappenException();
        }
    }
}
