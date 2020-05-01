package io.spring2go.seata.core.service;

import io.spring2go.seata.common.exception.FrameworkException;
import io.spring2go.seata.common.exception.NotSupportYetException;
import io.spring2go.seata.common.util.StringUtils;
import io.spring2go.seata.config.Configuration;
import io.spring2go.seata.config.ConfigurationFactory;

import static io.spring2go.seata.common.exception.FrameworkErrorCode.InvalidConfiguration;
import static io.spring2go.seata.core.service.ConfigurationKeys.SERVER_NODE_SPLIT_CHAR;

/**
 * Created by william on May, 2020
 */
public class ServiceManagerStaticConfigImpl implements ServiceManager {
    private Configuration configuration = ConfigurationFactory.getInstance();

    @Override
    public void register(String txServiceGroup, String address) {
        throw new NotSupportYetException("Dynamic registry");

    }

    @Override
    public void unregister(String txServiceGroup, String address) {
        throw new NotSupportYetException("Dynamic un-registry");

    }

    @Override
    public void watch(String txServiceGroup, AddressWatcher watcher) {
        throw new NotSupportYetException("Watch");

    }

    private String[] serverAddresses = null;

    @Override
    public String[] lookup(String txServiceGroup) {
        if (serverAddresses != null) {
            return serverAddresses;
        }
        String rGroup = configuration.getConfig(ConfigurationKeys.SERVICE_GROUP_MAPPING_PREFIX + txServiceGroup);
        if (rGroup == null) {
            throw new FrameworkException(InvalidConfiguration);
        }
        String rGroupDataId = ConfigurationKeys.SERVICE_PREFIX + rGroup + ConfigurationKeys.GROUPLIST_POSTFIX;
        String serverListConfig = configuration.getConfig(rGroupDataId);
        if (StringUtils.isEmpty(serverListConfig)) {
            throw new FrameworkException(InvalidConfiguration);
        }
        serverAddresses = serverListConfig.split(SERVER_NODE_SPLIT_CHAR);
        if (serverAddresses == null || serverAddresses.length == 0) {
            throw new FrameworkException(InvalidConfiguration);
        }
        return serverAddresses;

    }
}
