package io.spring2go.seata.core.protocol;

import io.netty.buffer.ByteBuf;

/**
 * Created by william on May, 2020
 */
public interface MessageCodec {
    short getTypeCode();
    byte[] encode();
    boolean decode(ByteBuf in);
}
