package io.spring2go.seata.core.rpc;

import io.spring2go.seata.core.protocol.AbstractMessage;
import io.spring2go.seata.core.protocol.AbstractResultMessage;

/**
 * Created by william on May, 2020
 */
public interface TransactionMessageHandler {
    /**
     * On a request received.
     *
     * @param request received request message
     * @param context context of the RPC
     * @return response to the request
     */
    AbstractResultMessage onRequest(AbstractMessage request, RpcContext context);

    /**
     * On a response received.
     *
     * @param response received response message
     * @param context context of the RPC
     */
    void onResponse(AbstractResultMessage response, RpcContext context);
}
