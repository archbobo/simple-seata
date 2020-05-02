package io.spring2go.seata.rm.datasource.sql;

/**
 * Created by william on May, 2020
 */
public interface SQLRecognizer {
    /**
     * Type of the SQL. INSERT/UPDATE/DELETE ...
     *
     * @return
     */
    SQLType getSQLType();

    /**
     * TableRecords source related in the SQL, including alias if any.
     * SELECT id, name FROM user u WHERE ...
     * TableRecords source should be 'user u' for this SQL.
     *
     * @return table source.
     */
    String getTableSource();

    /**
     * TableRecords name related in the SQL.
     * SELECT id, name FROM user u WHERE ...
     * TableRecords name should be 'user' for this SQL, without alias 'u'.
     *
     * @see #getTableSource()
     * @return table name.
     */
    String getTableName();

    /**
     * Return the original SQL input by the upper application.
     *
     * @return The original SQL.
     */
    String getOriginalSQL();
}
