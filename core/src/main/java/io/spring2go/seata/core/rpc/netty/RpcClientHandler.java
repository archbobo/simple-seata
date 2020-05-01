package io.spring2go.seata.core.rpc.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.collection.LongObjectHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by william on May, 2020
 */
public class RpcClientHandler extends ChannelDuplexHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RpcClientHandler.class);

    private final LongObjectHashMap compressTable = new LongObjectHashMap(8192, 0.5f);

    /**
     * Instantiates a new Rpc client handler.
     */
    public RpcClientHandler() {

    }

    @Override
    @SuppressWarnings("unchecked")
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        final Channel channel = ctx.channel();

        final String request = (String)msg;
        try {
            ctx.writeAndFlush(request, ctx.voidPromise());
            LOGGER.info("client:" + msg);

        } catch (Exception e) {
            LOGGER.error("when try flush error", e);
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        LOGGER.info("channel active for ClientProxyHandler at :[{}]",ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        LOGGER.info("channel inactive for ClientProxyHandler at :[{}]",ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        LOGGER.info("channel error for ClientProxyHandler at :[{}]",ctx.channel());
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        LOGGER.info("channel write for ClientProxyHandler at :[{}]",msg);
        ctx.write(msg, promise);
    }

    @Override
    public void flush(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("channel flush for ClientProxyHandler at :[{}]",ctx.channel());
        ctx.flush();
    }
}
