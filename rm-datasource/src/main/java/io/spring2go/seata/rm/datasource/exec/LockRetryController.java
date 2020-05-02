package io.spring2go.seata.rm.datasource.exec;

import io.spring2go.seata.config.ConfigurationFactory;
import io.spring2go.seata.core.service.ConfigurationKeys;

/**
 * Created by william on May, 2020
 */
public class LockRetryController {
    private static int LOCK_RETRY_INTERNAL =
            ConfigurationFactory.getInstance().getInt(ConfigurationKeys.CLIENT_LOCK_RETRY_INTERNAL, 10);
    private static int LOCK_RETRY_TIMES =
            ConfigurationFactory.getInstance().getInt(ConfigurationKeys.CLIENT_LOCK_RETRY_TIMES, 30);

    private int lockRetryInternal = LOCK_RETRY_INTERNAL;
    private int lockRetryTimes = LOCK_RETRY_TIMES;

    public LockRetryController() {
    }

    public void sleep(Exception e) throws LockWaitTimeoutException {
        if (--lockRetryTimes < 0) {
            throw new LockWaitTimeoutException("Global lock wait timeout", e);
        }

        try {
            Thread.sleep(lockRetryInternal);
        } catch (InterruptedException ignore) {
        }
    }
}
