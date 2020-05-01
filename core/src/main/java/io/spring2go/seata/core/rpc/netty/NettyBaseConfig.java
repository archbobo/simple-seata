package io.spring2go.seata.core.rpc.netty;

import io.netty.channel.Channel;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.EpollDomainSocketChannel;
import io.netty.channel.epoll.EpollServerDomainSocketChannel;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.kqueue.KQueueDomainSocketChannel;
import io.netty.channel.kqueue.KQueueServerDomainSocketChannel;
import io.netty.channel.kqueue.KQueueServerSocketChannel;
import io.netty.channel.kqueue.KQueueSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.NettyRuntime;
import io.netty.util.internal.PlatformDependent;
import io.spring2go.seata.config.Configuration;
import io.spring2go.seata.config.ConfigurationFactory;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by william on May, 2020
 */
public class NettyBaseConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyBaseConfig.class);

    /**
     * The constant CONFIG.
     */
    protected static final Configuration CONFIG = ConfigurationFactory.getInstance();
    /**
     * The constant BOSS_THREAD_PREFIX.
     */
    protected static final String BOSS_THREAD_PREFIX = CONFIG.getConfig("transport.thread-factory.boss-thread-prefix");

    /**
     * The constant WORKER_THREAD_PREFIX.
     */
    protected static final String WORKER_THREAD_PREFIX = CONFIG.getConfig(
            "transport.thread-factory.worker-thread-prefix");

    /**
     * The constant SHARE_BOSS_WORKER.
     */
    protected static final boolean SHARE_BOSS_WORKER = CONFIG.getBoolean("transport.thread-factory.share-boss-worker");

    /**
     * The constant WORKER_THREAD_SIZE.
     */
    protected static int WORKER_THREAD_SIZE;

    /**
     * The constant TRANSPORT_SERVER_TYPE.
     */
    protected static final TransportServerType TRANSPORT_SERVER_TYPE;

    /**
     * The constant SERVER_CHANNEL_CLAZZ.
     */
    protected static final Class<? extends ServerChannel> SERVER_CHANNEL_CLAZZ;
    /**
     * The constant CLIENT_CHANNEL_CLAZZ.
     */
    protected static final Class<? extends Channel> CLIENT_CHANNEL_CLAZZ;

    /**
     * The constant TRANSPORT_PROTOCOL_TYPE.
     */
    protected static final TransportProtocolType TRANSPORT_PROTOCOL_TYPE;

    protected static final int MAX_WRITE_IDLE_SECONDS = 0;

    protected static final int MAX_READ_IDLE_SECONDS = MAX_WRITE_IDLE_SECONDS * 3;

    protected static final int MAX_ALL_IDLE_SECONDS = 0;

    static {
        TRANSPORT_PROTOCOL_TYPE = TransportProtocolType.valueOf(CONFIG.getConfig("transport.type"));
        String workerThreadSize = CONFIG.getConfig("transport.thread-factory.worker-thread-size");
        if (StringUtils.isNotBlank(workerThreadSize) && StringUtils.isNumericSpace(workerThreadSize)) {
            WORKER_THREAD_SIZE = Integer.parseInt(workerThreadSize);
        } else if (null != WorkThreadMode.getModeByName(workerThreadSize)) {
            WORKER_THREAD_SIZE = WorkThreadMode.getModeByName(workerThreadSize).getValue();
        } else {
            WORKER_THREAD_SIZE = WorkThreadMode.Default.getValue();
        }
        TRANSPORT_SERVER_TYPE = TransportServerType.valueOf(CONFIG.getConfig("transport.server"));
        switch (TRANSPORT_SERVER_TYPE) {
            case NIO:
                if (TRANSPORT_PROTOCOL_TYPE == TransportProtocolType.TCP) {
                    SERVER_CHANNEL_CLAZZ = NioServerSocketChannel.class;
                    CLIENT_CHANNEL_CLAZZ = NioSocketChannel.class;
                } else {
                    raiseUnsupportedTransportError();
                    SERVER_CHANNEL_CLAZZ = null;
                    CLIENT_CHANNEL_CLAZZ = null;
                }
                break;
            case NATIVE:
                if (PlatformDependent.isWindows()) {
                    throw new IllegalArgumentException("no native supporting for Windows.");
                } else if (PlatformDependent.isOsx()) {
                    if (TRANSPORT_PROTOCOL_TYPE == TransportProtocolType.TCP) {
                        SERVER_CHANNEL_CLAZZ = KQueueServerSocketChannel.class;
                        CLIENT_CHANNEL_CLAZZ = KQueueSocketChannel.class;
                    } else if (TRANSPORT_PROTOCOL_TYPE == TransportProtocolType.UNIX_DOMAIN_SOCKET) {
                        SERVER_CHANNEL_CLAZZ = KQueueServerDomainSocketChannel.class;
                        CLIENT_CHANNEL_CLAZZ = KQueueDomainSocketChannel.class;
                    } else {
                        raiseUnsupportedTransportError();
                        SERVER_CHANNEL_CLAZZ = null;
                        CLIENT_CHANNEL_CLAZZ = null;
                    }
                } else {
                    if (TRANSPORT_PROTOCOL_TYPE == TransportProtocolType.TCP) {
                        SERVER_CHANNEL_CLAZZ = EpollServerSocketChannel.class;
                        CLIENT_CHANNEL_CLAZZ = EpollSocketChannel.class;
                    } else if (TRANSPORT_PROTOCOL_TYPE == TransportProtocolType.UNIX_DOMAIN_SOCKET) {
                        SERVER_CHANNEL_CLAZZ = EpollServerDomainSocketChannel.class;
                        CLIENT_CHANNEL_CLAZZ = EpollDomainSocketChannel.class;
                    } else {
                        raiseUnsupportedTransportError();
                        SERVER_CHANNEL_CLAZZ = null;
                        CLIENT_CHANNEL_CLAZZ = null;
                    }
                }
                break;
            default:
                throw new IllegalArgumentException("unsupported.");
        }
    }

    private static void raiseUnsupportedTransportError() throws RuntimeException {
        String errMsg = String.format("Unsupported provider type :[{}] for transport:[{}].", TRANSPORT_SERVER_TYPE,
                TRANSPORT_PROTOCOL_TYPE);
        LOGGER.error(errMsg);
        throw new IllegalArgumentException(errMsg);
    }

    /**
     * The enum Work thread mode.
     */
    enum WorkThreadMode {

        /**
         * Auto work thread mode.
         */
        Auto(NettyRuntime.availableProcessors() * 2 + 1),
        /**
         * Pin work thread mode.
         */
        Pin(NettyRuntime.availableProcessors()),
        /**
         * Busy pin work thread mode.
         */
        busyPin(NettyRuntime.availableProcessors() + 1),
        /**
         * Default work thread mode.
         */
        Default(NettyRuntime.availableProcessors() * 2);

        /**
         * Gets value.
         *
         * @return the value
         */
        public int getValue() {
            return value;
        }

        private int value;

        WorkThreadMode(int value) {
            this.value = value;
        }

        /**
         * Gets mode by name.
         *
         * @param name the name
         * @return the mode by name
         */
        public static WorkThreadMode getModeByName(String name) {
            if (Auto.name().equalsIgnoreCase(name)) {
                return Auto;
            } else if (Pin.name().equalsIgnoreCase(name)) {
                return Pin;
            } else if (busyPin.name().equalsIgnoreCase(name)) {
                return busyPin;
            } else if (Default.name().equalsIgnoreCase(name)) {
                return Default;
            } else {
                return null;
            }
        }

    }
}
