package io.spring2go.seata.core.protocol.transaction;

import java.nio.ByteBuffer;

/**
 * Created by william on May, 2020
 */
public abstract class AbstractTransactionRequestToTC extends AbstractTransactionRequest {
    protected ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

    protected TCInboundHandler handler;

    public void setTCInboundHandler(TCInboundHandler handler) {
        this.handler = handler;
    }
}
