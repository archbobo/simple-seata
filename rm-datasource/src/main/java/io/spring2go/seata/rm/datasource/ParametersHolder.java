package io.spring2go.seata.rm.datasource;

import java.util.ArrayList;

/**
 * Created by william on May, 2020
 */
public interface ParametersHolder {
    ArrayList<Object>[] getParameters();
}
