package io.spring2go.seata.rm.datasource.undo;

import io.spring2go.seata.rm.datasource.sql.SQLType;
import io.spring2go.seata.rm.datasource.sql.struct.TableMeta;
import io.spring2go.seata.rm.datasource.sql.struct.TableRecords;

/**
 * Created by william on May, 2020
 */
public class SQLUndoLog {
    private SQLType sqlType;

    private String tableName;

    private TableRecords beforeImage;

    private TableRecords afterImage;

    public void setTableMeta(TableMeta tableMeta) {
        if (beforeImage != null) {
            beforeImage.setTableMeta(tableMeta);
        }
        if (afterImage != null) {
            afterImage.setTableMeta(tableMeta);
        }
    }

    public SQLType getSqlType() {
        return sqlType;
    }

    public void setSqlType(SQLType sqlType) {
        this.sqlType = sqlType;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public TableRecords getBeforeImage() {
        return beforeImage;
    }

    public void setBeforeImage(TableRecords beforeImage) {
        this.beforeImage = beforeImage;
    }

    public TableRecords getAfterImage() {
        return afterImage;
    }

    public void setAfterImage(TableRecords afterImage) {
        this.afterImage = afterImage;
    }
}
