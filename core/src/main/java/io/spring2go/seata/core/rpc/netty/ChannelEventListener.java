package io.spring2go.seata.core.rpc.netty;

import io.netty.channel.Channel;

/**
 * Created by william on May, 2020
 */
public interface ChannelEventListener {
    /**
     * On channel connect.
     *
     * @param remoteAddr the remote addr
     * @param channel    the channel
     */
    void onChannelConnect(final String remoteAddr, final Channel channel);

    /**
     * On channel close.
     *
     * @param remoteAddr the remote addr
     * @param channel    the channel
     */
    void onChannelClose(final String remoteAddr, final Channel channel);

    /**
     * On channel exception.
     *
     * @param remoteAddr the remote addr
     * @param channel    the channel
     */
    void onChannelException(final String remoteAddr, final Channel channel);

    /**
     * On channel idle.
     *
     * @param remoteAddr the remote addr
     * @param channel    the channel
     */
    void onChannelIdle(final String remoteAddr, final Channel channel);
}
