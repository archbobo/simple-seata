package io.spring2go.seata.rm.datasource.exec;

import io.spring2go.seata.rm.datasource.ParametersHolder;
import io.spring2go.seata.rm.datasource.StatementProxy;
import io.spring2go.seata.rm.datasource.sql.SQLRecognizer;
import io.spring2go.seata.rm.datasource.sql.SQLUpdateRecognizer;
import io.spring2go.seata.rm.datasource.sql.struct.Field;
import io.spring2go.seata.rm.datasource.sql.struct.TableMeta;
import io.spring2go.seata.rm.datasource.sql.struct.TableRecords;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by william on May, 2020
 */
public class UpdateExecutor<T, S extends Statement> extends AbstractDMLBaseExecutor<T, S> {
    public UpdateExecutor(StatementProxy statementProxy, StatementCallback statementCallback, SQLRecognizer sqlRecognizer) {
        super(statementProxy, statementCallback, sqlRecognizer);
    }

    @Override
    protected TableRecords beforeImage() throws SQLException {
        SQLUpdateRecognizer visitor = (SQLUpdateRecognizer) sqlRecognizer;
        TableMeta tmeta = getTableMeta();
        List<String> updateColumns = visitor.getUpdateColumns();

        StringBuffer selectSQLAppender = new StringBuffer("SELECT ");
        if (!tmeta.containsPK(updateColumns)) {
            // PK should be included.
            selectSQLAppender.append(tmeta.getPkName() + ", ");
        }
        for (int i = 0; i < updateColumns.size(); i++) {
            selectSQLAppender.append(updateColumns.get(i));
            if (i < (updateColumns.size() - 1)) {
                selectSQLAppender.append(", ");
            }
        }
        String whereCondition = null;
        ArrayList<Object> paramAppender = new ArrayList<>();
        if (statementProxy instanceof ParametersHolder) {
            whereCondition = visitor.getWhereCondition((ParametersHolder) statementProxy, paramAppender);
        } else {
            whereCondition = visitor.getWhereCondition();
        }
        selectSQLAppender.append(" FROM " + tmeta.getTableName() + " WHERE " + whereCondition + " FOR UPDATE");
        String selectSQL = selectSQLAppender.toString();

        TableRecords beforeImage = null;
        PreparedStatement ps = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            if (paramAppender.isEmpty()) {
                st = statementProxy.getConnection().createStatement();
                rs = st.executeQuery(selectSQL);
            } else {
                ps = statementProxy.getConnection().prepareStatement(selectSQL);
                for (int i = 0; i< paramAppender.size(); i++) {
                    ps.setObject(i + 1, paramAppender.get(i));
                }
                rs = ps.executeQuery();
            }
            beforeImage = TableRecords.buildRecords(tmeta, rs);

        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
        return beforeImage;
    }

    @Override
    protected TableRecords afterImage(TableRecords beforeImage) throws SQLException {
        SQLUpdateRecognizer visitor = (SQLUpdateRecognizer) sqlRecognizer;
        TableMeta tmeta = getTableMeta();
        if (beforeImage == null || beforeImage.size() == 0) {
            return TableRecords.empty(getTableMeta());
        }
        List<String> updateColumns = visitor.getUpdateColumns();

        StringBuffer selectSQLAppender = new StringBuffer("SELECT ");
        if (!tmeta.containsPK(updateColumns)) {
            // PK should be included.
            selectSQLAppender.append(tmeta.getPkName() + ", ");
        }
        for (int i = 0; i < updateColumns.size(); i++) {
            selectSQLAppender.append(updateColumns.get(i));
            if (i < (updateColumns.size() - 1)) {
                selectSQLAppender.append(", ");
            }
        }
        List<Field> pkRows = beforeImage.pkRows();
        selectSQLAppender.append(" FROM " + tmeta.getTableName() + " WHERE " + buildWhereConditionByPKs(pkRows) + " FOR UPDATE");
        String selectSQL = selectSQLAppender.toString();

        TableRecords afterImage = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = statementProxy.getConnection().prepareStatement(selectSQL);
            int index = 0;
            for (Field pkField : pkRows) {
                index++;
                pst.setObject(index, pkField.getValue(), pkField.getType());
            }
            rs = pst.executeQuery();
            afterImage = TableRecords.buildRecords(tmeta, rs);

        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pst != null) {
                pst.close();
            }
        }
        return afterImage;
    }
}
