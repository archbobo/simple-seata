package io.spring2go.seata.core.protocol.transaction;

import io.spring2go.seata.core.model.BranchType;
import io.spring2go.seata.core.protocol.MergedMessage;
import io.spring2go.seata.core.rpc.RpcContext;

import java.nio.ByteBuffer;

/**
 * Created by william on May, 2020
 */
public class BranchRegisterRequest extends AbstractTransactionRequestToTC implements MergedMessage {
    private static final long serialVersionUID = 1242711598812634704L;

    private long transactionId;

    private BranchType branchType = BranchType.AT;

    private String resourceId;

    private String lockKey;

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public BranchType getBranchType() {
        return branchType;
    }

    public void setBranchType(BranchType branchType) {
        this.branchType = branchType;
    }

    public String getLockKey() {
        return lockKey;
    }

    public void setLockKey(String lockKey) {
        this.lockKey = lockKey;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    @Override
    public short getTypeCode() {
        return TYPE_BRANCH_REGISTER;
    }

    @Override
    public byte[] encode() {
        byte[] lockKeyBytes = null;
        if (this.lockKey != null) {
            lockKeyBytes = lockKey.getBytes(UTF8);
            if (lockKeyBytes.length > 512) {
                byteBuffer = ByteBuffer.allocate(lockKeyBytes.length + 1024);
            }
        }

        // 1. Transaction Id
        byteBuffer.putLong(this.transactionId);
        // 2. Branch Type
        byteBuffer.put((byte) this.branchType.ordinal());
        // 3. Resource Id
        if (this.resourceId != null) {
            byte[] bs = resourceId.getBytes(UTF8);
            byteBuffer.putShort((short) bs.length);
            if (bs.length > 0) {
                byteBuffer.put(bs);
            }
        } else {
            byteBuffer.putShort((short) 0);
        }

        // 4. Lock Key
        if (this.lockKey != null) {
            byteBuffer.putInt(lockKeyBytes.length);
            if (lockKeyBytes.length > 0) {
                byteBuffer.put(lockKeyBytes);
            }
        } else {
            byteBuffer.putInt(0);
        }

        byteBuffer.flip();
        byte[] content = new byte[byteBuffer.limit()];
        byteBuffer.get(content);
        return content;
    }

    @Override
    public void decode(ByteBuffer byteBuffer) {
        this.transactionId = byteBuffer.getLong();
        this.branchType = BranchType.get(byteBuffer.get());
        short len = byteBuffer.getShort();
        if (len > 0) {
            byte[] bs = new byte[len];
            byteBuffer.get(bs);
            this.setResourceId(new String(bs, UTF8));
        }

        int iLen = byteBuffer.getInt();
        if (iLen > 0) {
            byte[] bs = new byte[iLen];
            byteBuffer.get(bs);
            this.setLockKey(new String(bs, UTF8));
        }
    }


    @Override
    public AbstractTransactionResponse handle(RpcContext rpcContext) {
        return handler.handle(this, rpcContext);
    }

}
