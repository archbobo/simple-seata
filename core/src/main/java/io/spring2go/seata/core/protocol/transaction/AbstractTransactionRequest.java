package io.spring2go.seata.core.protocol.transaction;

import io.spring2go.seata.core.protocol.AbstractMessage;
import io.spring2go.seata.core.rpc.RpcContext;

import java.nio.ByteBuffer;

/**
 * Created by william on May, 2020
 */
public abstract class AbstractTransactionRequest extends AbstractMessage {
    protected ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

    public abstract AbstractTransactionResponse handle(RpcContext rpcContext);
}
