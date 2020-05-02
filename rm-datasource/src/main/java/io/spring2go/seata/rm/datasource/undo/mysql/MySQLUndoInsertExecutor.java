package io.spring2go.seata.rm.datasource.undo.mysql;

import io.spring2go.seata.common.exception.ShouldNeverHappenException;
import io.spring2go.seata.rm.datasource.sql.struct.Field;
import io.spring2go.seata.rm.datasource.sql.struct.KeyType;
import io.spring2go.seata.rm.datasource.sql.struct.Row;
import io.spring2go.seata.rm.datasource.sql.struct.TableRecords;
import io.spring2go.seata.rm.datasource.undo.AbstractUndoExecutor;
import io.spring2go.seata.rm.datasource.undo.SQLUndoLog;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by william on May, 2020
 */
public class MySQLUndoInsertExecutor extends AbstractUndoExecutor {
    @Override
    protected String buildUndoSQL() {
        TableRecords afterImage = sqlUndoLog.getAfterImage();
        List<Row> afterImageRows = afterImage.getRows();
        if (afterImageRows == null || afterImageRows.size() == 0) {
            throw new ShouldNeverHappenException("Invalid UNDO LOG");
        }
        Row row = afterImageRows.get(0);
        StringBuffer mainSQL = new StringBuffer("DELETE FROM " + sqlUndoLog.getTableName());
        StringBuffer where = new StringBuffer(" WHERE ");
        boolean first = true;
        for (Field field : row.getFields()) {
            if (field.getKeyType() == KeyType.PrimaryKey) {
                where.append(field.getName() + " = ? ");
            }

        }
        return mainSQL.append(where).toString();
    }

    @Override
    protected void undoPrepare(PreparedStatement undoPST, ArrayList<Field> undoValues, Field pkValue) throws SQLException {
        undoPST.setObject(1, pkValue.getValue(), pkValue.getType());
    }

    public MySQLUndoInsertExecutor(SQLUndoLog sqlUndoLog) {
        super(sqlUndoLog);
    }

    @Override
    protected TableRecords getUndoRows() {
        return sqlUndoLog.getAfterImage();
    }
}
