package io.spring2go.seata.rm.datasource.exec;

import java.sql.SQLException;

/**
 * Created by william on May, 2020
 */
public class LockWaitTimeoutException extends SQLException {
    private static final long serialVersionUID = -6754599774015964707L;

    public LockWaitTimeoutException() {
    }

    public LockWaitTimeoutException(String reason, Throwable cause) {
        super(reason, cause);
    }

    public LockWaitTimeoutException(Throwable e) {
        super(e);
    }
}
