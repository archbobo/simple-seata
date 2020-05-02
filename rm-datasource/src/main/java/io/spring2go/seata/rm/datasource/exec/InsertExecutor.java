package io.spring2go.seata.rm.datasource.exec;

import io.spring2go.seata.common.exception.NotSupportYetException;
import io.spring2go.seata.common.exception.ShouldNeverHappenException;
import io.spring2go.seata.rm.datasource.PreparedStatementProxy;
import io.spring2go.seata.rm.datasource.StatementProxy;
import io.spring2go.seata.rm.datasource.sql.SQLInsertRecognizer;
import io.spring2go.seata.rm.datasource.sql.SQLRecognizer;
import io.spring2go.seata.rm.datasource.sql.struct.ColumnMeta;
import io.spring2go.seata.rm.datasource.sql.struct.TableMeta;
import io.spring2go.seata.rm.datasource.sql.struct.TableRecords;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by william on May, 2020
 */
public class InsertExecutor<T, S extends Statement> extends AbstractDMLBaseExecutor<T, S> {
    public InsertExecutor(StatementProxy statementProxy, StatementCallback statementCallback, SQLRecognizer sqlRecognizer) {
        super(statementProxy, statementCallback, sqlRecognizer);
    }

    @Override
    protected TableRecords beforeImage() throws SQLException {
        return TableRecords.empty(getTableMeta());
    }

    @Override
    protected TableRecords afterImage(TableRecords beforeImage) throws SQLException {
        SQLInsertRecognizer visitor = (SQLInsertRecognizer) sqlRecognizer;
        List<String> insertColumns = visitor.getInsertColumns();
        TableMeta tmeta = getTableMeta();
        TableRecords afterImage = null;
        if (tmeta.containsPK(insertColumns)) {
            // insert values including PK
            List<Object> pkValues = null;
            String pk = tmeta.getPkName();
            for (int paramIdx = 0; paramIdx < insertColumns.size(); paramIdx++) {
                if (insertColumns.get(paramIdx).equalsIgnoreCase(pk)) {
                    if (statementProxy instanceof PreparedStatementProxy) {
                        pkValues = ((PreparedStatementProxy) statementProxy).getParamsByIndex(paramIdx);
                    } else {
                        List<List<Object>> insertRows = visitor.getInsertRows();
                        pkValues = new ArrayList<>(insertRows.size());
                        for (List<Object> row : insertRows) {
                            pkValues.add(row.get(paramIdx));
                        }
                    }
                    break;
                }
            }
            if (pkValues == null) {
                throw new ShouldNeverHappenException();
            }
            afterImage = getTableRecords(pkValues);

        } else {
            // PK is just auto generated
            Map<String, ColumnMeta> pkMetaMap = getTableMeta().getPrimaryKeyMap();
            if (pkMetaMap.size() != 1) {
                throw new NotSupportYetException();
            }
            ColumnMeta pkMeta = pkMetaMap.values().iterator().next();
            if (!pkMeta.isAutoincrement()) {
                throw new ShouldNeverHappenException();
            }

            ResultSet genKeys = null;
            try {
                genKeys = statementProxy.getTargetStatement().getGeneratedKeys();
            } catch (SQLException e) {
                // java.sql.SQLException: Generated keys not requested. You need to
                // specify Statement.RETURN_GENERATED_KEYS to
                // Statement.executeUpdate() or Connection.prepareStatement().
                if ("S1009".equalsIgnoreCase(e.getSQLState())) {
                    genKeys = statementProxy.getTargetStatement().executeQuery("SELECT LAST_INSERT_ID()");
                } else {
                    throw e;
                }
            }
            List<Object> pkValues = new ArrayList<>();
            while (genKeys.next()) {
                Object v = genKeys.getObject(1);
                pkValues.add(v);
            }

            afterImage = getTableRecords(pkValues);

        }

        if (afterImage == null) {
            throw new SQLException("Failed to build after-image for insert");
        }

        return afterImage;
    }

    private TableRecords getTableRecords(List<Object> pkValues) throws SQLException {
        TableRecords afterImage;
        String pk = getTableMeta().getPkName();
        StringBuffer selectSQLAppender = new StringBuffer("SELECT * FROM " + getTableMeta().getTableName() + " WHERE ");
        for (int i = 1; i <= pkValues.size(); i++) {
            selectSQLAppender.append(pk + "=?");
            if (i < pkValues.size()) {
                selectSQLAppender.append(" OR ");
            }
        }
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = statementProxy.getConnection().prepareStatement(selectSQLAppender.toString());

            for (int i = 1; i <= pkValues.size(); i++) {
                ps.setObject(i, pkValues.get(i - 1));
            }

            rs = ps.executeQuery();
            afterImage = TableRecords.buildRecords(getTableMeta(), rs);

        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
        return afterImage;
    }
}
