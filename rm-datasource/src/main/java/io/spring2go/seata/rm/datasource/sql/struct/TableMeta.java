package io.spring2go.seata.rm.datasource.sql.struct;

import io.spring2go.seata.common.exception.NotSupportYetException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by william on May, 2020
 */
public class TableMeta {
    private String tableName;

    private Map<String, ColumnMeta> allColumns = new HashMap<String, ColumnMeta>();
    private Map<String, IndexMeta> allIndexes = new HashMap<String, IndexMeta>();

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public ColumnMeta getColumnMeta(String colName) {
        String s = colName.toUpperCase();
        ColumnMeta col = allColumns.get(s);
        if (col == null) {
            if (colName.charAt(0) == '`') {
                col = allColumns.get(s.substring(1, colName.length() - 1));
            } else { col = allColumns.get("`" + s + "`"); }
        }
        return col;
    }

    public Map<String, ColumnMeta> getAllColumns() {
        return allColumns;
    }

    public Map<String, IndexMeta> getAllIndexes() {
        return allIndexes;
    }

    public ColumnMeta getAutoIncreaseColumn() {
        // TODO: how about auto increment but not pk?
        for (Map.Entry<String, ColumnMeta> entry : allColumns.entrySet()) {
            ColumnMeta col = entry.getValue();
            if ("YES".equalsIgnoreCase(col.getIsAutoincrement()) == true) {
                return col;
            }
        }
        return null;
    }

    public Map<String, ColumnMeta> getPrimaryKeyMap() {
        Map<String, ColumnMeta> pk = new HashMap<String, ColumnMeta>();
        for (Map.Entry<String, IndexMeta> entry : allIndexes.entrySet()) {
            IndexMeta index = entry.getValue();
            if (index.getIndextype().value() == IndexType.PRIMARY.value()) {
                for (ColumnMeta col : index.getValues()) {
                    pk.put(col.getColumnName().toUpperCase(), col);
                }
            }
        }

        if (pk.size() > 1) {
            throw new NotSupportYetException("Multi PK");
        }

        return pk;
    }

    @SuppressWarnings("serial")
    public List<String> getPrimaryKeyOnlyName() {
        return new ArrayList<String>() {
            {
                for (Map.Entry<String, ColumnMeta> entry : getPrimaryKeyMap().entrySet()) {
                    add(entry.getKey());
                }
            }
        };
    }

    public String getPkName() {
        return getPrimaryKeyOnlyName().get(0);
    }

    public boolean containsPK(List<String> cols) {
        if (cols == null) {
            return false;
        }

        List<String> pk = getPrimaryKeyOnlyName();
        if (pk.isEmpty()) {
            return false;
        }

        return cols.containsAll(pk);
    }

    public String getCreateTableSQL() {
        StringBuilder sb = new StringBuilder("CREATE TABLE");
        sb.append(String.format(" `%s` ", getTableName()));
        sb.append("(");

        boolean flag = true;
        Map<String, ColumnMeta> allColumns = getAllColumns();
        for (Map.Entry<String, ColumnMeta> entry : allColumns.entrySet()) {
            if (flag == true) {
                flag = false;
            } else {
                sb.append(",");
            }

            ColumnMeta col = entry.getValue();
            sb.append(String.format(" `%s` ", col.getColumnName()));
            sb.append(col.getDataTypeName());
            if (col.getColumnSize() > 0) {
                sb.append(String.format("(%d)", col.getColumnSize()));
            }

            if (col.getColumnDef() != null && col.getColumnDef().length() > 0) {
                sb.append(String.format(" default '%s'", col.getColumnDef()));
            }

            if (col.getIsNullAble() != null && col.getIsNullAble().length() > 0) {
                sb.append(" ");
                sb.append(col.getIsNullAble());
            }
        }

        Map<String, IndexMeta> allIndexes = getAllIndexes();
        for (Map.Entry<String, IndexMeta> entry : allIndexes.entrySet()) {
            sb.append(", ");

            IndexMeta index = entry.getValue();
            switch (index.getIndextype()) {
                case FullText:
                    break;
                case Normal:
                    sb.append(String.format("KEY `%s`", index.getIndexName()));
                    break;
                case PRIMARY:
                    sb.append(String.format("PRIMARY KEY"));
                    break;
                case Unique:
                    sb.append(String.format("UNIQUE KEY `%s`", index.getIndexName()));
                    break;
                default:
                    break;
            }

            sb.append(" (");
            boolean f = true;
            for (ColumnMeta c : index.getValues()) {
                if (f == true) {
                    f = false;
                } else {
                    sb.append(",");
                }

                sb.append(String.format("`%s`", c.getColumnName()));
            }
            sb.append(")");
        }
        sb.append(")");

        return sb.toString();
    }
}
