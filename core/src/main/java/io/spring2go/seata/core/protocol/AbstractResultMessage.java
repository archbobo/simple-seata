package io.spring2go.seata.core.protocol;

import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;

/**
 * Created by william on May, 2020
 */
public abstract class AbstractResultMessage extends AbstractMessage implements MergedMessage {
    private static final long serialVersionUID = 6540352050650203313L;

    private ResultCode resultCode;

    public ByteBuffer byteBuffer = ByteBuffer.allocate(512);

    public ResultCode getResultCode() {
        return resultCode;
    }

    public void setResultCode(ResultCode resultCode) {
        this.resultCode = resultCode;
    }

    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    protected void doEncode() {
        byteBuffer.put((byte) resultCode.ordinal());
        if (resultCode == ResultCode.Failed) {
            if (getMsg() != null) {
                String msg;
                if (getMsg().length() > 128) {
                    msg = getMsg().substring(0, 128);
                } else {
                    msg = getMsg();
                }
                byte[] bs = msg.getBytes(UTF8);
                if (bs.length > 400 && getMsg().length() > 64) {
                    msg = getMsg().substring(0, 64);
                    bs = msg.getBytes(UTF8);
                }
                byteBuffer.putShort((short) bs.length);
                if (bs.length > 0) {
                    byteBuffer.put(bs);
                }
            } else {
                byteBuffer.putShort((short) 0);
            }
        }
    }

    private final byte[] flushEncode() {
        byteBuffer.flip();
        byte[] content = new byte[byteBuffer.limit()];
        byteBuffer.get(content);
        byteBuffer.clear(); // >?
        return content;
    }

    @Override
    public final byte[] encode() {
        doEncode();
        return flushEncode();
    }

    @Override
    public void decode(ByteBuffer byteBuffer) {
        setResultCode(ResultCode.get(byteBuffer.get()));
        if (resultCode == ResultCode.Failed) {
            short len = byteBuffer.getShort();
            if (len > 0) {
                byte[] msg = new byte[len];
                byteBuffer.get(msg);
                this.setMsg(new String(msg, UTF8));
            }
        }
    }

    @Override
    public boolean decode(ByteBuf in) {
        int i = in.readableBytes();
        if (i < 1) {
            return false;
        }
        setResultCode(ResultCode.get(in.readByte()));
        i--;
        if (resultCode == ResultCode.Failed) {
            if (i < 2) {
                return false;
            }
            short len = in.readShort();
            i -= 2;
            if (i < len) {
                return false;
            }

            if (len > 0) {
                byte[] msg = new byte[len];
                in.readBytes(msg);
                this.setMsg(new String(msg, UTF8));
            }
        }
        return true;
    }
}
