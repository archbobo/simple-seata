package io.spring2go.seata.core.protocol;

import java.nio.ByteBuffer;

/**
 * Created by william on May, 2020
 */
public interface MergedMessage {
    void decode(ByteBuffer byteBuffer);
}
