package io.spring2go.seata.core.protocol.transaction;

import java.nio.ByteBuffer;

/**
 * Created by william on May, 2020
 */
public abstract class AbstractTransactionRequestToRM extends AbstractTransactionRequest {
    protected ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

    protected RMInboundHandler handler;

    public void setRMInboundMessageHandler(RMInboundHandler handler) {
        this.handler = handler;
    }
}
