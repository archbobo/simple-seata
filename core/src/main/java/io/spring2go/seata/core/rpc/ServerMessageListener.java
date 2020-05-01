package io.spring2go.seata.core.rpc;

import io.netty.channel.ChannelHandlerContext;
import io.spring2go.seata.core.protocol.RegisterRMRequest;
import io.spring2go.seata.core.protocol.RegisterTMRequest;
import io.spring2go.seata.core.rpc.netty.RegisterCheckAuthHandler;

/**
 * Created by william on May, 2020
 */
public interface ServerMessageListener {

    /**
     * On trx message.
     *
     * @param msgId   the msg id
     * @param ctx     the ctx
     * @param message the message
     * @param sender  the sender
     */
    void onTrxMessage(long msgId, ChannelHandlerContext ctx, Object message, ServerMessageSender sender);

    /**
     * On reg rm message.
     *
     * @param msgId            the msg id
     * @param ctx              the ctx
     * @param message          the message
     * @param sender           the sender
     * @param checkAuthHandler the check auth handler
     */
    void onRegRmMessage(long msgId, ChannelHandlerContext ctx, RegisterRMRequest message,
                        ServerMessageSender sender, RegisterCheckAuthHandler checkAuthHandler);

    /**
     * On reg tm message.
     *
     * @param msgId            the msg id
     * @param ctx              the ctx
     * @param message          the message
     * @param sender           the sender
     * @param checkAuthHandler the check auth handler
     */
    void onRegTmMessage(long msgId, ChannelHandlerContext ctx, RegisterTMRequest message,
                        ServerMessageSender sender, RegisterCheckAuthHandler checkAuthHandler);

    /**
     * On check message.
     *
     * @param msgId  the msg id
     * @param ctx    the ctx
     * @param sender the sender
     */
    void onCheckMessage(long msgId, ChannelHandlerContext ctx, ServerMessageSender sender);
}
