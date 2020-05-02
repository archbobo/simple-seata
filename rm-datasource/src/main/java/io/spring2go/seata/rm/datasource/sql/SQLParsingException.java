package io.spring2go.seata.rm.datasource.sql;

/**
 * Created by william on May, 2020
 */
public class SQLParsingException extends RuntimeException {
    public SQLParsingException(String message) {
        super(message);
    }

    public SQLParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    public SQLParsingException(Throwable cause) {
        super(cause);
    }
}
