package io.spring2go.seata.rm.datasource.undo.mysql;

import io.spring2go.seata.common.exception.ShouldNeverHappenException;
import io.spring2go.seata.rm.datasource.sql.struct.Field;
import io.spring2go.seata.rm.datasource.sql.struct.KeyType;
import io.spring2go.seata.rm.datasource.sql.struct.Row;
import io.spring2go.seata.rm.datasource.sql.struct.TableRecords;
import io.spring2go.seata.rm.datasource.undo.AbstractUndoExecutor;
import io.spring2go.seata.rm.datasource.undo.SQLUndoLog;

import java.util.List;

/**
 * Created by william on May, 2020
 */
public class MySQLUndoUpdateExecutor extends AbstractUndoExecutor {
    @Override
    protected String buildUndoSQL() {
        TableRecords beforeImage = sqlUndoLog.getBeforeImage();
        List<Row> beforeImageRows = beforeImage.getRows();
        if (beforeImageRows == null || beforeImageRows.size() == 0) {
            throw new ShouldNeverHappenException("Invalid UNDO LOG"); // TODO
        }
        Row row = beforeImageRows.get(0);
        StringBuffer mainSQL = new StringBuffer("UPDATE " + sqlUndoLog.getTableName() + " SET ");
        StringBuffer where = new StringBuffer(" WHERE ");
        boolean first = true;
        for (Field field : row.getFields()) {
            if (field.getKeyType() == KeyType.PrimaryKey) {
                where.append(field.getName() + " = ?");
            } else {
                if (first) {
                    first = false;
                } else {
                    mainSQL.append(", ");
                }
                mainSQL.append(field.getName() + " = ?");
            }

        }
        return mainSQL.append(where).toString();
    }

    public MySQLUndoUpdateExecutor(SQLUndoLog sqlUndoLog) {
        super(sqlUndoLog);
    }

    @Override
    protected TableRecords getUndoRows() {
        return sqlUndoLog.getBeforeImage();
    }
}
