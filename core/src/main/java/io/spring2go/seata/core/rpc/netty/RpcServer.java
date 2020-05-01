package io.spring2go.seata.core.rpc.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.spring2go.seata.common.exception.FrameworkErrorCode;
import io.spring2go.seata.common.exception.FrameworkException;
import io.spring2go.seata.common.util.NetUtil;
import io.spring2go.seata.core.protocol.HeartbeatMessage;
import io.spring2go.seata.core.protocol.RegisterRMRequest;
import io.spring2go.seata.core.protocol.RegisterTMRequest;
import io.spring2go.seata.core.protocol.RpcMessage;
import io.spring2go.seata.core.rpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeoutException;

/**
 * Created by william on May, 2020
 */
@Sharable
public class RpcServer extends AbstractRpcRemotingServer implements ServerMessageSender {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServer.class);

    /**
     * The Server message listener.
     */
    protected ServerMessageListener serverMessageListener;

    private TransactionMessageHandler transactionMessageHandler;
    private RegisterCheckAuthHandler checkAuthHandler;

    /**
     * Sets transactionMessageHandler.
     *
     * @param transactionMessageHandler the transactionMessageHandler
     */
    public void setHandler(TransactionMessageHandler transactionMessageHandler) {
        setHandler(transactionMessageHandler, null);
    }

    /**
     * Sets transactionMessageHandler.
     *
     * @param transactionMessageHandler the transactionMessageHandler
     * @param checkAuthHandler          the check auth handler
     */
    public void setHandler(TransactionMessageHandler transactionMessageHandler,
                           RegisterCheckAuthHandler checkAuthHandler) {
        this.transactionMessageHandler = transactionMessageHandler;
        this.checkAuthHandler = checkAuthHandler;
    }

    /**
     * Instantiates a new Abstract rpc server.
     *
     * @param messageExecutor the message executor
     */
    public RpcServer(ThreadPoolExecutor messageExecutor) {
        super(new NettyServerConfig(), messageExecutor);
    }

    /**
     * Gets server message listener.
     *
     * @return the server message listener
     */
    public ServerMessageListener getServerMessageListener() {
        return serverMessageListener;
    }

    /**
     * Sets server message listener.
     *
     * @param serverMessageListener the server message listener
     */
    public void setServerMessageListener(ServerMessageListener serverMessageListener) {
        this.serverMessageListener = serverMessageListener;
    }

    /**
     * Debug log.
     *
     * @param info the info
     */
    public void debugLog(String info) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(info);
        }
    }

    /**
     * Init.
     */
    @Override
    public void init() {
        super.init();
        setChannelHandlers(RpcServer.this);
        DefaultServerMessageListenerImpl defaultServerMessageListenerImpl = new DefaultServerMessageListenerImpl(
                transactionMessageHandler);
        defaultServerMessageListenerImpl.setServerMessageSender(this);
        this.setServerMessageListener(defaultServerMessageListenerImpl);
        super.start();

    }

    private void closeChannelHandlerContext(ChannelHandlerContext ctx) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("closeChannelHandlerContext channel:" + ctx.channel());
        }
        ctx.disconnect();
        ctx.close();
    }

    /**
     * User event triggered.
     *
     * @param ctx the ctx
     * @param evt the evt
     * @throws Exception the exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof IdleStateEvent) {
            debugLog("idle:" + evt);
            IdleStateEvent idleStateEvent = (IdleStateEvent)evt;
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("channel:" + ctx.channel() + " read idle.");
                }
                handleDisconnect(ctx);
                try {
                    closeChannelHandlerContext(ctx);
                } catch (Exception e) {
                    LOGGER.error(e.getMessage());
                }
            }
        }
    }

    /**
     * Destroy.
     */
    @Override
    public void destroy() {
        super.destroy();
        super.shutdown();
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("destroyed rpcServer");
        }
    }

    /**
     * Send request.
     * handle asyn branch commit and rollback
     *
     * @param dbKey         the db key
     * @param clientIp      the client ip
     * @param clientAppName the client app name
     * @param msg           the msg
     */
    @Override
    public void sendRequest(String dbKey, String clientIp, String clientAppName, Object msg) {
        Channel clientChannel = ChannelManager.getChannel(dbKey, clientIp, clientAppName);
        if (clientChannel == null) {
            throw new FrameworkException("rm client is not connected. dbkey:" + dbKey
                    + ",clientIp:" + clientIp);
        } else {
            try {
                super.sendRequest(clientChannel, msg);
            } catch (FrameworkException e) {
                if (e.getErrcode() == FrameworkErrorCode.ChannelIsNotWritable) {
                    ChannelManager.releaseRpcContext(clientChannel);
                    throw e;
                }
            }
        }
    }

    /**
     * Send response.
     * redress,merge msg
     *
     * @param msgId         the msg id
     * @param dbKey         the db key
     * @param clientIp      the client ip
     * @param clientAppName the client app name
     * @param msg           the msg
     */
    @Override
    public void sendResponse(long msgId, String dbKey, String clientIp, String clientAppName,
                             Object msg) {
        Channel clientChannel = ChannelManager.getChannel(dbKey, clientIp, clientAppName);
        if (clientChannel != null) {
            try {
                super.sendResponse(msgId, clientChannel, msg);
            } catch (FrameworkException e) {
                if (e.getErrcode() == FrameworkErrorCode.ChannelIsNotWritable) {
                    LOGGER.error("channel is not writeable,channel:" + clientChannel);
                    ChannelManager.releaseRpcContext(clientChannel);
                }
                throw e;
            }
        } else {
            throw new RuntimeException("channel is error. channel:" + clientChannel);
        }
    }

    /**
     * Send response.
     * rm reg,rpc reg,inner response
     *
     * @param msgId   the msg id
     * @param channel the channel
     * @param msg     the msg
     */
    @Override
    public void sendResponse(long msgId, Channel channel, Object msg) {
        Channel clientChannel = channel;
        if (!(msg instanceof HeartbeatMessage)) {
            clientChannel = ChannelManager.getSameClientChannel(channel);
        }
        if (clientChannel != null) {
            super.sendResponse(msgId, clientChannel, msg);
        } else {
            throw new RuntimeException("channel is error. channel:" + clientChannel);
        }
    }

    /**
     * Send request with response object.
     * send syn request for rm
     *
     * @param dbKey         the db key
     * @param clientIp      the client ip
     * @param clientAppName the client app name
     * @param msg           the msg
     * @param timeout       the timeout
     * @return the object
     * @throws TimeoutException the timeout exception
     */
    @Override
    public Object sendSynRequest(String dbKey, String clientIp, String clientAppName, Object msg,
                                 long timeout) throws TimeoutException {
        Channel clientChannel = ChannelManager.getChannel(dbKey, clientIp, clientAppName);
        if (clientChannel != null) {
            return super.sendAsyncRequestWithResponse(null, clientChannel, msg, timeout);
        } else {
            throw new RuntimeException("rm client is not connected. dbkey:" + dbKey
                    + ",clientIp:" + clientIp);
        }
    }

    /**
     * Send request with response object.
     *
     * @param dbKey         the db key
     * @param clientIp      the client ip
     * @param clientAppName the client app name
     * @param msg           the msg
     * @return the object
     * @throws TimeoutException the timeout exception
     */
    @Override
    public Object sendSynRequest(String dbKey, String clientIp, String clientAppName, Object msg)
            throws TimeoutException {
        return sendSynRequest(dbKey, clientIp, clientAppName, msg, NettyServerConfig.getRpcRequestTimeout());
    }

    /**
     * Dispatch.
     *
     * @param msgId the msg id
     * @param ctx   the ctx
     * @param msg   the msg
     */
    @Override
    public void dispatch(long msgId, ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof RegisterRMRequest) {
            serverMessageListener.onRegRmMessage(msgId, ctx, (RegisterRMRequest)msg, this,
                    checkAuthHandler);
        } else {
            if (ChannelManager.isRegistered(ctx.channel())) {
                serverMessageListener.onTrxMessage(msgId, ctx, msg, this);
            } else {
                try {
                    closeChannelHandlerContext(ctx);
                } catch (Exception exx) {
                    LOGGER.error(exx.getMessage());
                }
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info(String.format("close a unhandled connection! [%s]", ctx.channel().toString()));
                }
            }
        }
    }

    /**
     * Channel inactive.
     *
     * @param ctx the ctx
     * @throws Exception the exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        debugLog("inactive:" + ctx);
        if (messageExecutor.isShutdown()) {
            return;
        }
        handleDisconnect(ctx);
        super.channelInactive(ctx);
    }

    private void handleDisconnect(ChannelHandlerContext ctx) {
        final String ipAndPort = NetUtil.toStringAddress(ctx.channel().remoteAddress());
        RpcContext rpcContext = ChannelManager.getContextFromIdentified(ctx.channel());
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(ipAndPort + " to server channel inactive.");
        }
        if (null != rpcContext && null != rpcContext.getClientRole()) {
            rpcContext.release();
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("remove channel:" + ctx.channel() + "context:" + rpcContext);
            }
        } else {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("remove unuse channel:" + ctx.channel());
            }
        }
    }

    /**
     * Channel read.
     *
     * @param ctx the ctx
     * @param msg the msg
     * @throws Exception the exception
     */
    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof RpcMessage) {
            RpcMessage rpcMessage = (RpcMessage)msg;
            debugLog("read:" + rpcMessage.getBody().toString());
            if (rpcMessage.getBody() instanceof RegisterTMRequest) {
                RegisterTMRequest request
                        = (RegisterTMRequest)rpcMessage
                        .getBody();
                serverMessageListener.onRegTmMessage(rpcMessage.getId(), ctx, request, this, checkAuthHandler);
                return;
            }
            if (rpcMessage.getBody() == HeartbeatMessage.PING) {
                serverMessageListener.onCheckMessage(rpcMessage.getId(), ctx, this);
                return;
            }
        }
        super.channelRead(ctx, msg);
    }

    /**
     * Exception caught.
     *
     * @param ctx   the ctx
     * @param cause the cause
     * @throws Exception the exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("channel exx:" + cause.getMessage() + ",channel:" + ctx.channel());
        }
        ChannelManager.releaseRpcContext(ctx.channel());
        super.exceptionCaught(ctx, cause);
    }
}
