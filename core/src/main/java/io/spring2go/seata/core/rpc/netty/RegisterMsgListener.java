package io.spring2go.seata.core.rpc.netty;

import io.netty.channel.Channel;
import io.spring2go.seata.core.protocol.AbstractMessage;

/**
 * Created by william on May, 2020
 */
public interface RegisterMsgListener {
    /**
     * On register msg success.
     *
     * @param serverAddress  the server address
     * @param channel        the channel
     * @param response       the response
     * @param requestMessage the request message
     */
    void onRegisterMsgSuccess(String serverAddress, Channel channel, Object response, AbstractMessage requestMessage);

    /**
     * On register msg fail.
     *
     * @param serverAddress  the server address
     * @param channel        the channel
     * @param response       the response
     * @param requestMessage the request message
     */
    void onRegisterMsgFail(String serverAddress, Channel channel, Object response, AbstractMessage requestMessage);
}
