package io.spring2go.seata.core.rpc;

/**
 * Created by william on May, 2020
 */
public interface RemotingService {
    /**
     * Start.
     */
    void start();

    /**
     * Shutdown.
     */
    void shutdown();
}
