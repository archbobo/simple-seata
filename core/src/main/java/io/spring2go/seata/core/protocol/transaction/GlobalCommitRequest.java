package io.spring2go.seata.core.protocol.transaction;

import io.spring2go.seata.core.rpc.RpcContext;

/**
 * Created by william on May, 2020
 */
public class GlobalCommitRequest extends AbstractGlobalEndRequest {
    @Override
    public short getTypeCode() {
        return TYPE_GLOBAL_COMMIT;
    }

    @Override
    public AbstractTransactionResponse handle(RpcContext rpcContext) {
        return handler.handle(this, rpcContext);
    }

}
