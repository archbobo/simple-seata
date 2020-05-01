package io.spring2go.seata.core.protocol.transaction;

import io.spring2go.seata.core.protocol.MergedMessage;
import io.spring2go.seata.core.rpc.RpcContext;

import java.nio.ByteBuffer;

/**
 * Created by william on May, 2020
 */
public class GlobalBeginRequest extends AbstractTransactionRequestToTC implements MergedMessage {
    private static final long serialVersionUID = 7236162274218388376L;

    private int timeout = 60000;

    private String transactionName;

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    @Override
    public short getTypeCode() {
        return TYPE_GLOBAL_BEGIN;
    }

    @Override
    public byte[] encode() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(256);
        byteBuffer.putInt(timeout);

        if (this.transactionName != null) {
            byte[] bs = transactionName.getBytes(UTF8);
            byteBuffer.putShort((short) bs.length);
            if (bs.length > 0) {
                byteBuffer.put(bs);
            }
        } else {
            byteBuffer.putShort((short) 0);
        }

        byteBuffer.flip();
        byte[] content = new byte[byteBuffer.limit()];
        byteBuffer.get(content);
        return content;
    }

    @Override
    public void decode(ByteBuffer byteBuffer) {
        this.timeout = byteBuffer.getInt();

        short len = byteBuffer.getShort();
        if (len > 0) {
            byte[] bs = new byte[len];
            byteBuffer.get(bs);
            this.setTransactionName(new String(bs, UTF8));
        }
    }

    @Override
    public AbstractTransactionResponse handle(RpcContext rpcContext) {
        return handler.handle(this, rpcContext);
    }

}
