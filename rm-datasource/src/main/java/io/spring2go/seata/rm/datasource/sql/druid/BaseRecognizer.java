package io.spring2go.seata.rm.datasource.sql.druid;

import io.spring2go.seata.rm.datasource.sql.SQLRecognizer;

/**
 * Created by william on May, 2020
 */
public abstract class BaseRecognizer implements SQLRecognizer {
    public static class VMarker {
        @Override
        public String toString() {
            return "?";
        }

    }

    protected String originalSQL;

    public BaseRecognizer(String originalSQL) {
        this.originalSQL = originalSQL;

    }

    @Override
    public String getOriginalSQL() {
        return originalSQL;
    }
}
