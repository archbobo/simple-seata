package io.spring2go.seata.core.protocol;

import io.netty.buffer.ByteBuf;

import java.io.Serializable;

/**
 * Created by william on May, 2020
 */
public class RegisterRMRequest extends AbstractIdentifyRequest implements Serializable {
    private static final long serialVersionUID = 7539732523682335742L;

    private String resourceIds;

    public RegisterRMRequest() {
        this(null, null);
    }

    public RegisterRMRequest(String applicationId, String transactionServiceGroup) {
        super(applicationId, transactionServiceGroup);
    }

    public String getResourceIds() {
        return resourceIds;
    }

    public void setResourceIds(String resourceIds) {
        this.resourceIds = resourceIds;
    }
    @Override
    public short getTypeCode() {
        return TYPE_REG_RM;
    }

    @Override
    protected void doEncode() {
        super.doEncode();
        if (this.resourceIds != null) {
            byte[] bs = resourceIds.getBytes(UTF8);
            byteBuffer.putInt(bs.length);
            if (bs.length > 0) {
                byteBuffer.put(bs);
            }
        } else {
            byteBuffer.putInt(0);
        }
    }

    @Override
    public boolean decode(ByteBuf in) {
        int i = in.readableBytes();
        if (i < 1) {
            return false;
        }

        short len = in.readShort();
        if (len > 0) {
            if (i < len) {
                return false;
            } else {
                i -= len;
            }
            byte[] bs = new byte[len];
            in.readBytes(bs);
            this.setVersion(new String(bs, UTF8));
        }

        len = in.readShort();
        if (len > 0) {
            if (i < len) {
                return false;
            } else {
                i -= len;
            }
            byte[] bs = new byte[len];
            in.readBytes(bs);
            this.setApplicationId(new String(bs, UTF8));
        }

        len = in.readShort();
        if (len > 0) {
            if (i < len) {
                return false;
            } else {
                i -= len;
            }
            byte[] bs = new byte[len];
            in.readBytes(bs);
            this.setTransactionServiceGroup(new String(bs, UTF8));
        }

        len = in.readShort();
        if (len > 0) {
            if (i < len) {
                return false;
            } else {
                i -= len;
            }
            byte[] bs = new byte[len];
            in.readBytes(bs);
            this.setExtraData(new String(bs, UTF8));
        }

        int iLen = in.readInt();
        if (iLen > 0) {
            if (i < iLen) {
                return false;
            } else {
                i -= iLen;
            }
            byte[] bs = new byte[iLen];
            in.readBytes(bs);
            this.setResourceIds(new String(bs, UTF8));
        }
        return true;
    }

    @Override
    public String toString() {
        return "RegisterRMRequest{" +
                "resourceIds='" + resourceIds + '\'' +
                ", applicationId='" + applicationId + '\'' +
                ", transactionServiceGroup='" + transactionServiceGroup + '\'' +
                '}';
    }
}
