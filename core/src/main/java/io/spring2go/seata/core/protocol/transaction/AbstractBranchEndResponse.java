package io.spring2go.seata.core.protocol.transaction;

import io.netty.buffer.ByteBuf;
import io.spring2go.seata.core.model.BranchStatus;

import java.nio.ByteBuffer;

/**
 * Created by william on May, 2020
 */
public abstract class AbstractBranchEndResponse extends AbstractTransactionResponse {
    protected BranchStatus branchStatus;

    public BranchStatus getBranchStatus() {
        return branchStatus;
    }

    public void setBranchStatus(BranchStatus branchStatus) {
        this.branchStatus = branchStatus;
    }

    @Override
    protected void doEncode() {
        super.doEncode();
        byteBuffer.put((byte) branchStatus.ordinal());
    }

    @Override
    public void decode(ByteBuffer byteBuffer) {
        super.decode(byteBuffer);
        branchStatus = BranchStatus.get(byteBuffer.get());
    }

    @Override
    public boolean decode(ByteBuf in) {
        boolean s = super.decode(in);
        if (!s) {
            return s;
        }
        branchStatus = BranchStatus.get(in.readByte());
        return true;
    }
}
