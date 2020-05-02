package io.spring2go.seata.rm.datasource.exec;

import io.spring2go.seata.core.context.RootContext;
import io.spring2go.seata.rm.datasource.StatementProxy;
import io.spring2go.seata.rm.datasource.sql.SQLRecognizer;
import io.spring2go.seata.rm.datasource.sql.struct.Field;
import io.spring2go.seata.rm.datasource.sql.struct.TableMeta;
import io.spring2go.seata.rm.datasource.sql.struct.TableMetaCache;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Created by william on May, 2020
 */
public abstract class BaseTransactionalExecutor<T, S extends Statement> implements Executor {
    protected StatementProxy<S> statementProxy;

    protected StatementCallback<T, S> statementCallback;

    protected SQLRecognizer sqlRecognizer;

    private TableMeta tableMeta;

    public BaseTransactionalExecutor(StatementProxy<S> statementProxy, StatementCallback<T, S> statementCallback, SQLRecognizer sqlRecognizer) {
        this.statementProxy = statementProxy;
        this.statementCallback = statementCallback;
        this.sqlRecognizer = sqlRecognizer;
    }

    @Override
    public Object execute(Object... args) throws Throwable {
        String xid = RootContext.getXID();
        statementProxy.getConnectionProxy().bind(xid);
        return doExecute(args);
    }

    protected abstract Object doExecute(Object... args) throws Throwable;

    protected String buildWhereConditionByPKs(List<Field> pkRows) throws SQLException {
        StringBuffer whereConditionAppender = new StringBuffer();
        for (int i = 0; i < pkRows.size(); i++) {
            Field field = pkRows.get(i);
            whereConditionAppender.append(field.getName() + " = ?");
            if (i < (pkRows.size() - 1)) {
                whereConditionAppender.append(" OR ");
            }
        }
        return whereConditionAppender.toString();
    }

    protected TableMeta getTableMeta() {
        return getTableMeta(sqlRecognizer.getTableName());
    }

    protected TableMeta getTableMeta(String tableName) {
        if (tableMeta != null) {
            return tableMeta;
        }
        tableMeta = TableMetaCache.getTableMeta(statementProxy.getConnectionProxy().getDataSourceProxy(), tableName);
        return tableMeta;
    }
}
