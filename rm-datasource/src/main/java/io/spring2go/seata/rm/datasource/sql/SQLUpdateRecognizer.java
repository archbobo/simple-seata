package io.spring2go.seata.rm.datasource.sql;

import java.util.List;

/**
 * Created by william on May, 2020
 */
public interface SQLUpdateRecognizer extends WhereRecognizer {

    List<String> getUpdateColumns();

    List<Object> getUpdateValues();
}
