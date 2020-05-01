package io.spring2go.seata.core.protocol.transaction;

import io.netty.buffer.ByteBuf;
import io.spring2go.seata.core.exception.TransactionExceptionCode;
import io.spring2go.seata.core.protocol.AbstractResultMessage;

import java.nio.ByteBuffer;

/**
 * Created by william on May, 2020
 */
public abstract class AbstractTransactionResponse extends AbstractResultMessage {
    private TransactionExceptionCode transactionExceptionCode = TransactionExceptionCode.Unknown;

    public TransactionExceptionCode getTransactionExceptionCode() {
        return transactionExceptionCode;
    }

    public void setTransactionExceptionCode(TransactionExceptionCode transactionExceptionCode) {
        this.transactionExceptionCode = transactionExceptionCode;
    }

    @Override
    protected void doEncode() {
        super.doEncode();
        byteBuffer.put((byte) transactionExceptionCode.ordinal());
    }

    @Override
    public void decode(ByteBuffer byteBuffer) {
        super.decode(byteBuffer);
        transactionExceptionCode = TransactionExceptionCode.get(byteBuffer.get());
    }

    @Override
    public boolean decode(ByteBuf in) {
        boolean s = super.decode(in);
        if (!s) {
            return s;
        }
        transactionExceptionCode = TransactionExceptionCode.get(in.readByte());
        return true;
    }
}
