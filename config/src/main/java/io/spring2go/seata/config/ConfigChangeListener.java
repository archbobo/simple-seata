package io.spring2go.seata.config;

import java.util.concurrent.Executor;

public interface ConfigChangeListener {
    /**
     * Gets executor.
     *
     * @return the executor
     */
    Executor getExecutor();

    /**
     * Receive config info.
     *
     * @param configInfo the config info
     */
    void receiveConfigInfo(final String configInfo);
}
