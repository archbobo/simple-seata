package io.spring2go.seata.core.rpc.netty;

import io.netty.channel.Channel;
import io.netty.channel.EventLoop;
import io.netty.channel.pool.ChannelHealthChecker;
import io.netty.util.concurrent.Future;

/**
 * Created by william on May, 2020
 */
public interface ChannelAuthHealthChecker extends ChannelHealthChecker {
    /**
     * The constant ACTIVE.
     */
    ChannelAuthHealthChecker ACTIVE = new ChannelAuthHealthChecker() {
        @Override
        public Future<Boolean> isHealthy(Channel channel) {
            EventLoop loop = channel.eventLoop();
            return channel.isActive()? loop.newSucceededFuture(Boolean.TRUE) : loop.newSucceededFuture(Boolean.FALSE);
        }
    };
}
