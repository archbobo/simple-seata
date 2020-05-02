package io.spring2go.seata.rm.datasource.sql;

import io.spring2go.seata.rm.datasource.ParametersHolder;

import java.util.ArrayList;

/**
 * Created by william on May, 2020
 */
public interface WhereRecognizer extends SQLRecognizer {
    String getWhereCondition(ParametersHolder parametersHolder, ArrayList<Object> paramAppender);

    String getWhereCondition();
}
