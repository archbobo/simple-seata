package io.spring2go.seata.core.rpc.netty;

import io.spring2go.seata.core.protocol.RegisterRMRequest;
import io.spring2go.seata.core.protocol.RegisterTMRequest;

/**
 * Created by william on May, 2020
 */
public interface RegisterCheckAuthHandler {
    /**
     * Reg transaction manager check auth boolean.
     *
     * @param request the request
     * @return the boolean
     */
    boolean regTransactionManagerCheckAuth(RegisterTMRequest request);

    /**
     * Reg resource manager chec k auth boolean.
     *
     * @param request the request
     * @return the boolean
     */
    boolean regResourceManagerChecKAuth(RegisterRMRequest request);
}
