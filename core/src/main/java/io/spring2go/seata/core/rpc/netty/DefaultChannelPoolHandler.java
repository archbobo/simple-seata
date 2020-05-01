package io.spring2go.seata.core.rpc.netty;

import io.netty.channel.Channel;
import io.netty.channel.pool.AbstractChannelPoolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by william on May, 2020
 */
public class DefaultChannelPoolHandler extends AbstractChannelPoolHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultChannelPoolHandler.class);

    @Override
    public void channelCreated(Channel ch) throws Exception {

    }
}
