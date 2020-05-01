package io.spring2go.seata.core.protocol.transaction;

import io.spring2go.seata.core.model.BranchStatus;
import io.spring2go.seata.core.protocol.MergedMessage;
import io.spring2go.seata.core.rpc.RpcContext;

import java.nio.ByteBuffer;

/**
 * Created by william on May, 2020
 */
public class BranchReportRequest extends AbstractTransactionRequestToTC implements MergedMessage {
    private long transactionId;

    private long branchId;

    private String resourceId;

    private BranchStatus status;

    private String applicationData;

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

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public BranchStatus getStatus() {
        return status;
    }

    public void setStatus(BranchStatus status) {
        this.status = status;
    }

    public String getApplicationData() {
        return applicationData;
    }

    public void setApplicationData(String applicationData) {
        this.applicationData = applicationData;
    }

    @Override
    public short getTypeCode() {
        return TYPE_BRANCH_STATUS_REPORT;
    }

    @Override
    public byte[] encode() {
        byte[] applicationDataBytes = null;
        if (this.applicationData != null) {
            applicationDataBytes = applicationData.getBytes(UTF8);
            if (applicationDataBytes.length > 512) {
                byteBuffer = ByteBuffer.allocate(applicationDataBytes.length + 1024);
            }
        }

        // 1. Transaction Id
        byteBuffer.putLong(this.transactionId);
        // 2. Branch Id
        byteBuffer.putLong(this.branchId);
        // 3. Branch Status
        byteBuffer.put((byte) this.status.ordinal());
        // 4. Resource Id
        if (this.resourceId != null) {
            byte[] bs = resourceId.getBytes(UTF8);
            byteBuffer.putShort((short) bs.length);
            if (bs.length > 0) {
                byteBuffer.put(bs);
            }
        } else {
            byteBuffer.putShort((short) 0);
        }

        // 5. Application Data
        if (this.applicationData != null) {
            byteBuffer.putInt(applicationDataBytes.length);
            if (applicationDataBytes.length > 0) {
                byteBuffer.put(applicationDataBytes);
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
        this.branchId = byteBuffer.getLong();
        this.status = BranchStatus.get(byteBuffer.get());
        short len = byteBuffer.getShort();
        if (len > 0) {
            byte[] bs = new byte[len];
            byteBuffer.get(bs);
            this.resourceId = new String(bs, UTF8);
        }

        int iLen = byteBuffer.getInt();
        if (iLen > 0) {
            byte[] bs = new byte[iLen];
            byteBuffer.get(bs);
            this.applicationData = new String(bs, UTF8);
        }
    }

    @Override
    public AbstractTransactionResponse handle(RpcContext rpcContext) {
        return handler.handle(this, rpcContext);
    }

}
