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
public class MySQLUndoDeleteExecutor extends AbstractUndoExecutor {
    public MySQLUndoDeleteExecutor(SQLUndoLog sqlUndoLog) {
        super(sqlUndoLog);
    }

    @Override
    protected String buildUndoSQL() {
        TableRecords beforeImage = sqlUndoLog.getBeforeImage();
        List<Row> beforeImageRows = beforeImage.getRows();
        if (beforeImageRows == null || beforeImageRows.size() == 0) {
            throw new ShouldNeverHappenException("Invalid UNDO LOG");
        }
        Row row = beforeImageRows.get(0);

        StringBuffer insertColumns = new StringBuffer();
        StringBuffer insertValues = new StringBuffer();
        Field pkField = null;
        boolean first = true;
        for (Field field : row.getFields()) {
            if (field.getKeyType() == KeyType.PrimaryKey) {
                pkField = field;
                continue;
            } else {
                if (first) {
                    first = false;
                } else {
                    insertColumns.append(", ");
                    insertValues.append(", ");
                }
                insertColumns.append(field.getName());
                insertValues.append("?");
            }

        }
        if (first) {
            first = false;
        } else {
            insertColumns.append(", ");
            insertValues.append(", ");
        }
        insertColumns.append(pkField.getName());
        insertValues.append("?");

        return "INSERT INTO " + sqlUndoLog.getTableName() + "(" + insertColumns.toString() + ") VALUES (" + insertValues.toString() + ")";
    }

    @Override
    protected TableRecords getUndoRows() {
        return sqlUndoLog.getBeforeImage();
    }
}
