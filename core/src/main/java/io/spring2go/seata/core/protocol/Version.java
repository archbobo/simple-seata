package io.spring2go.seata.core.protocol;

import io.netty.channel.Channel;
import io.spring2go.seata.common.util.NetUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by william on May, 2020
 */
public class Version {
    public static final String CURRENT = "0.1.0";

    public static final Map<String, String> VERSION_MAP = new ConcurrentHashMap<String, String>();

    private Version() {

    }

    public static void putChannelVersion(Channel c, String v) {
        VERSION_MAP.put(NetUtil.toStringAddress(c.remoteAddress()), v);
    }

    public static String getChannelVersion(Channel c) {
        return VERSION_MAP.get(NetUtil.toStringAddress(c.remoteAddress()));
    }

    public static String checkVersion(String version) throws UncompatibleVersionException {
        // TODO: check
        return version;
    }
}
