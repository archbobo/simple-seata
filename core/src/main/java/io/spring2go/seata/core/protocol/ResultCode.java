package io.spring2go.seata.core.protocol;

/**
 * Created by william on May, 2020
 */
public enum ResultCode {
    // Failed
    Failed,

    // Success
    Success;

    public static ResultCode get(byte ordinal) {
        return get((int) ordinal);
    }
    public static ResultCode get(int ordinal) {
        for (ResultCode resultCode : ResultCode.values()) {
            if (resultCode.ordinal() == ordinal) {
                return resultCode;
            }
        }
        throw new IllegalArgumentException("Unknown ResultCode[" + ordinal + "]");
    }
}
