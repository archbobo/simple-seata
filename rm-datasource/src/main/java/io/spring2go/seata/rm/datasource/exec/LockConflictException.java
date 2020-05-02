package io.spring2go.seata.rm.datasource.exec;

import java.sql.SQLException;

/**
 * Created by william on May, 2020
 */
public class LockConflictException extends SQLException {
    public LockConflictException() {
    }
}
