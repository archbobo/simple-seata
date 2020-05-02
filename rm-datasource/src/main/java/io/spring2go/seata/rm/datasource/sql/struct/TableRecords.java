package io.spring2go.seata.rm.datasource.sql.struct;

import com.alibaba.fastjson.annotation.JSONField;
import io.spring2go.seata.common.exception.ShouldNeverHappenException;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by william on May, 2020
 */
public class TableRecords {
    @JSONField(serialize = false)
    private TableMeta tableMeta;

    private String tableName;

    private List<Row> rows = new ArrayList<Row>();

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    public TableRecords() {

    }

    public TableRecords(TableMeta tableMeta) {
        setTableMeta(tableMeta);
    }

    public void setTableMeta(TableMeta tableMeta) {
        if (this.tableMeta != null) {
            throw new ShouldNeverHappenException();
        }
        this.tableMeta = tableMeta;
        this.tableName = tableMeta.getTableName();
    }

    public int size() {
        return rows.size();
    }

    public void add(Row row) {
        rows.add(row);
    }

    public List<Field> pkRows() {
        final String pkName = getTableMeta().getPkName();
        return new ArrayList<Field>() {
            {
                for (Row row : rows) {
                    List<Field> fields = row.getFields();
                    for (Field field : fields) {
                        if (field.getName().equalsIgnoreCase(pkName)) {
                            add(field);
                            break;
                        }
                    }
                }
            }
        };
    }

    public TableMeta getTableMeta() {
        return tableMeta;
    }

    public static TableRecords empty(TableMeta tableMeta) {
        return new TableRecords(tableMeta) {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public List<Field> pkRows() {
                return new ArrayList<>();
            }

            @Override
            public void add(Row row) {
                throw new UnsupportedOperationException("xxx");
            }

            @Override
            public TableMeta getTableMeta() {
                throw new UnsupportedOperationException("xxx");
            }
        };
    }

    public static TableRecords buildRecords(TableMeta tmeta, ResultSet resultSet) throws SQLException {
        TableRecords records = new TableRecords(tmeta);
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int columnCount = resultSetMetaData.getColumnCount();

        while (resultSet.next()) {
            List<Field> fields = new ArrayList<>(columnCount);
            for (int i = 1; i <= columnCount; i++) {
                String colName = resultSetMetaData.getColumnName(i).toUpperCase();
                ColumnMeta col = tmeta.getColumnMeta(colName);
                Field field = new Field();
                field.setName(col.getColumnName());
                if (tmeta.getPkName().equals(field.getName())) {
                    field.setKeyType(KeyType.PrimaryKey);
                }
                field.setType(col.getDataType());
                field.setValue(resultSet.getObject(i));
                fields.add(field);
            }

            Row row = new Row();
            row.setFields(fields);

            records.add(row);
        }
        return records;
    }
}
