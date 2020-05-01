package io.spring2go.seata.core.rpc.netty;

/**
 * Created by william on May, 2020
 */
public enum  TransportServerType {
    /**
     * Native transport server type.
     */
    NATIVE("native"),
    /**
     * Nio transport server type.
     */
    NIO("nio");

    /**
     * The Name.
     */
    public final String name;

    TransportServerType(String name) {
        this.name = name;
    }
}
