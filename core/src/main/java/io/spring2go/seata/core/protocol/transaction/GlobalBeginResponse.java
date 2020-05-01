package io.spring2go.seata.core.protocol.transaction;

import io.spring2go.seata.core.protocol.AbstractMessage;

import java.nio.ByteBuffer;

/**
 * Created by william on May, 2020
 */
public class GlobalBeginResponse extends AbstractTransactionResponse {
    private static final long serialVersionUID = -5947172130577163908L;

    private String xid;

    private String extraData;

    public String getXid() {
        return xid;
    }

    public void setXid(String xid) {
        this.xid = xid;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    @Override
    public short getTypeCode() {
        return AbstractMessage.TYPE_GLOBAL_BEGIN_RESULT;
    }

    @Override
    protected void doEncode() {
        super.doEncode();

        if (this.xid != null) {
            byte[] bs = xid.getBytes(UTF8);
            byteBuffer.putShort((short) bs.length);
            if (bs.length > 0) {
                byteBuffer.put(bs);
            }
        } else {
            byteBuffer.putShort((short) 0);
        }

        if (this.extraData != null) {
            byte[] bs = extraData.getBytes(UTF8);
            byteBuffer.putShort((short) bs.length);
            if (bs.length > 0) {
                byteBuffer.put(bs);
            }
        } else {
            byteBuffer.putShort((short) 0);
        }
    }

    @Override
    public void decode(ByteBuffer byteBuffer) {
        super.decode(byteBuffer);

        short len = byteBuffer.getShort();
        if (len > 0) {
            byte[] bs = new byte[len];
            byteBuffer.get(bs);
            setXid(new String(bs, UTF8));
        }

        len = byteBuffer.getShort();
        if (len > 0) {
            byte[] bs = new byte[len];
            byteBuffer.get(bs);
            setExtraData(new String(bs, UTF8));
        }
    }
}
