package io.spring2go.seata.core.protocol.transaction;

import io.spring2go.seata.core.model.GlobalStatus;

import java.nio.ByteBuffer;

/**
 * Created by william on May, 2020
 */
public abstract class AbstractGlobalEndResponse extends AbstractTransactionResponse {

    protected GlobalStatus globalStatus;

    public GlobalStatus getGlobalStatus() {
        return globalStatus;
    }

    public void setGlobalStatus(GlobalStatus globalStatus) {
        this.globalStatus = globalStatus;
    }


    @Override
    protected void doEncode() {
        super.doEncode();
        byteBuffer.put((byte) globalStatus.ordinal());
    }

    @Override
    public void decode(ByteBuffer byteBuffer) {
        super.decode(byteBuffer);
        globalStatus = GlobalStatus.get(byteBuffer.get());
    }

}
