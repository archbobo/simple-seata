package io.spring2go.seata.core.rpc.netty;

import io.netty.channel.EventLoopGroup;

import java.util.concurrent.ThreadFactory;

/**
 * Created by william on May, 2020
 */
public interface RpcEventLoopGroup {
    /**
     * Create event loop group event loop group.
     *
     * @param workThreadSize the work thread size
     * @param threadFactory  the thread factory
     * @return the event loop group
     */
    EventLoopGroup createEventLoopGroup(int workThreadSize, ThreadFactory threadFactory);
}
