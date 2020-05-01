package io.spring2go.seata.core.rpc.netty;

/**
 * Created by william on May, 2020
 */
public enum  TransportProtocolType {
    /**
     * Tcp transport protocol type.
     */
    TCP("tcp"),

    /**
     * Udt transport protocol type.
     */
    UDT("udt"),
    /**
     * Unix domain socket transport protocol type.
     */
    UNIX_DOMAIN_SOCKET("unix-domain-socket");

    /**
     * The Name.
     */
    public final String name;

    TransportProtocolType(String name) {
        this.name = name;
    }
}
