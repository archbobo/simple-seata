package io.spring2go.seata.core.service;

/**
 * Created by william on May, 2020
 */
public class ConfigurationKeys {
    public static final String SERVICE_PREFIX = "service.";
    public static final String SERVICE_GROUP_MAPPING_PREFIX = SERVICE_PREFIX + "vgroup_mapping.";
    public static final String GROUPLIST_POSTFIX = ".grouplist";
    public static final String SERVER_NODE_SPLIT_CHAR = "\n";

    public static final String ENABLE_DEGRADE_POSTFIX = "enableDegrade";

    public static final String CLIENT_PREFIX = "client.";

    public static final String CLIENT_ASYNC_COMMIT_BUFFER_LIMIT = CLIENT_PREFIX + "async.commit.buffer.limit";
    public static final String CLIENT_LOCK_RETRY_TIMES = CLIENT_PREFIX + "lock.retry.times";
    public static final String CLIENT_LOCK_RETRY_INTERNAL = CLIENT_PREFIX + "lock.retry.internal";
}
