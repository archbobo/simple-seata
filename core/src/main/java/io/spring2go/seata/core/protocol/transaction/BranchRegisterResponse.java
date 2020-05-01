package io.spring2go.seata.core.protocol.transaction;

import io.spring2go.seata.core.protocol.AbstractMessage;

import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * Created by william on May, 2020
 */
public class BranchRegisterResponse extends AbstractTransactionResponse implements Serializable {
    private static final long serialVersionUID = 8317040433102745774L;

    private long transactionId;

    private long branchId;

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public long getBranchId() {
        return branchId;
    }

    public void setBranchId(long branchId) {
        this.branchId = branchId;
    }

    @Override
    public short getTypeCode() {
        return AbstractMessage.TYPE_BRANCH_REGISTER_RESULT;
    }

    @Override
    protected void doEncode() {
        super.doEncode();
        byteBuffer.putLong(transactionId);
        byteBuffer.putLong(branchId);

    }

    @Override
    public void decode(ByteBuffer byteBuffer) {
        super.decode(byteBuffer);
        this.transactionId = byteBuffer.getLong();
        this.branchId = byteBuffer.getLong();
    }
}
