package io.spring2go.seata.core.protocol.transaction;

import java.nio.ByteBuffer;

/**
 * Created by william on May, 2020
 */
public class GlobalLockQueryResponse extends AbstractTransactionResponse {
    private boolean lockable = false;

    public boolean isLockable() {
        return lockable;
    }

    public void setLockable(boolean lockable) {
        this.lockable = lockable;
    }

    @Override
    public short getTypeCode() {
        return TYPE_GLOBAL_LOCK_QUERY_RESULT;
    }


    @Override
    protected void doEncode() {
        super.doEncode();
        byteBuffer.putShort((short) (lockable ? 1 : 0));
    }

    @Override
    public void decode(ByteBuffer byteBuffer) {
        super.decode(byteBuffer);
        this.lockable = byteBuffer.getShort() == 1;
    }
}
