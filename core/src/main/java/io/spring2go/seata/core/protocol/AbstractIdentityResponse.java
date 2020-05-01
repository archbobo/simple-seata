package io.spring2go.seata.core.protocol;

import io.netty.buffer.ByteBuf;

/**
 * Created by william on May, 2020
 */
public abstract class AbstractIdentityResponse extends AbstractResultMessage {
    private String version = Version.CURRENT;;

    private String extraData;

    private boolean identified;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    public boolean isIdentified() {
        return identified;
    }

    public void setIdentified(boolean identified) {
        this.identified = identified;
    }
    @Override
    public void doEncode() {
//        super.doEncode();
        byteBuffer.put(this.identified ? (byte) 1 : (byte) 0);
        if (this.version != null) {
            byte[] bs = version.getBytes(UTF8);
            byteBuffer.putShort((short) bs.length);
            if (bs.length > 0) {
                byteBuffer.put(bs);
            }
        } else {
            byteBuffer.putShort((short) 0);
        }

    }

    @Override
    public boolean decode(ByteBuf in) {
        int i = in.readableBytes();
        if (i < 3) {
            return false;
        }
        i -= 3;
        this.identified = (in.readByte() == 1);

        short len = in.readShort();
        if (len > 0) {
            if (i < len) {
                return false;
            }

            byte[] bs = new byte[len];
            in.readBytes(bs);
            this.setVersion(new String(bs, UTF8));
        }

        return true;
    }
}
