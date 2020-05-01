package io.spring2go.seata.core.protocol.transaction;

import io.spring2go.seata.core.protocol.AbstractMessage;
import io.spring2go.seata.core.rpc.RpcContext;

/**
 * Created by william on May, 2020
 */
public class BranchCommitRequest extends AbstractBranchEndRequest {
    @Override
    public short getTypeCode() {
        return AbstractMessage.TYPE_BRANCH_COMMIT;
    }

    @Override
    public AbstractTransactionResponse handle(RpcContext rpcContext) {
        return handler.handle(this);
    }

}
