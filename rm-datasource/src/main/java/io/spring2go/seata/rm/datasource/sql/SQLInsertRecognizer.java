package io.spring2go.seata.rm.datasource.sql;

import java.util.List;

/**
 * Created by william on May, 2020
 */
public interface SQLInsertRecognizer extends SQLRecognizer {
    List<String> getInsertColumns();

    List<List<Object>> getInsertRows();
}
