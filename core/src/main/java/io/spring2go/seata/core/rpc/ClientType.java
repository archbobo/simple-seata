package io.spring2go.seata.core.rpc;

/**
 * Created by william on May, 2020
 */
public enum ClientType {
    // Transaction Manager client
    TM,

    // Resource Manager client
    RM;

    public static ClientType get(byte ordinal) {
        return get((int) ordinal);
    }

    public static ClientType get(int ordinal) {
        for (ClientType clientType : ClientType.values()) {
            if (clientType.ordinal() == ordinal) {
                return clientType;
            }
        }
        throw new IllegalArgumentException("Unknown ClientType[" + ordinal + "]");
    }
}
