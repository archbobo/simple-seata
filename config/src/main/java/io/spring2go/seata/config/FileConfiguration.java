package io.spring2go.seata.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.spring2go.seata.config.ConfigFuture.ConfigOperation;
import io.spring2go.seata.common.thread.NamedThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class FileConfiguration implements Configuration {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileConfiguration.class);

    private static final Config CONFIG = ConfigFactory.load();

    private ExecutorService configOperateExecutor;

    private static final int CORE_CONFIG_OPERATE_THREAD = 1;

    private static final int MAX_CONFIG_OPERATE_THREAD = 2;

    private static final long DEFAULT_CONFIG_TIMEOUT = 5 * 1000;

    /**
     * Instantiates a new File configuration.
     */
    public FileConfiguration() {
        configOperateExecutor = new ThreadPoolExecutor(CORE_CONFIG_OPERATE_THREAD, MAX_CONFIG_OPERATE_THREAD,
                Integer.MAX_VALUE, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(),
                new NamedThreadFactory("configOperate", MAX_CONFIG_OPERATE_THREAD, true));
    }

    @Override
    public int getInt(String dataId, int defaultValue, long timeoutMills) {
        String result = getConfig(dataId, String.valueOf(defaultValue), timeoutMills);
        return Integer.valueOf(result).intValue();
    }

    @Override
    public int getInt(String dataId, int defaultValue) {
        return getInt(dataId, defaultValue, DEFAULT_CONFIG_TIMEOUT);
    }

    @Override
    public int getInt(String dataId) {
        return getInt(dataId, 0);
    }

    @Override
    public long getLong(String dataId, long defaultValue, long timeoutMills) {
        String result = getConfig(dataId, String.valueOf(defaultValue), timeoutMills);
        return Long.valueOf(result).longValue();
    }

    @Override
    public long getLong(String dataId, long defaultValue) {
        return getLong(dataId, defaultValue, DEFAULT_CONFIG_TIMEOUT);
    }

    @Override
    public long getLong(String dataId) {
        return getLong(dataId, 0L);
    }

    @Override
    public boolean getBoolean(String dataId, boolean defaultValue, long timeoutMills) {
        String result = getConfig(dataId, String.valueOf(defaultValue), timeoutMills);
        return Boolean.valueOf(result).booleanValue();
    }

    @Override
    public boolean getBoolean(String dataId, boolean defaultValue) {
        return getBoolean(dataId, defaultValue, DEFAULT_CONFIG_TIMEOUT);
    }

    @Override
    public boolean getBoolean(String dataId) {
        return getBoolean(dataId, false);
    }

    @Override
    public String getConfig(String dataId, String defaultValue, long timeoutMills) {
        ConfigFuture configFuture = new ConfigFuture(dataId, defaultValue, ConfigFuture.ConfigOperation.GET, timeoutMills);
        configOperateExecutor.submit(new ConfigOperateRunnable(configFuture));
        return (String)configFuture.get();
    }

    @Override
    public String getConfig(String dataId, String defaultValue) {
        return getConfig(dataId, defaultValue, DEFAULT_CONFIG_TIMEOUT);
    }

    @Override
    public String getConfig(String dataId, long timeoutMills) {
        return getConfig(dataId, null, timeoutMills);
    }

    @Override
    public String getConfig(String dataId) {
        return getConfig(dataId, DEFAULT_CONFIG_TIMEOUT);
    }

    @Override
    public boolean putConfig(String dataId, String content, long timeoutMills) {
        ConfigFuture configFuture = new ConfigFuture(dataId, content, ConfigOperation.PUT, timeoutMills);
        configOperateExecutor.submit(new ConfigOperateRunnable(configFuture));
        return (Boolean)configFuture.get();
    }

    @Override
    public boolean putConfig(String dataId, String content) {
        return putConfig(dataId, content, DEFAULT_CONFIG_TIMEOUT);
    }

    @Override
    public boolean putConfigIfAbsent(String dataId, String content, long timeoutMills) {
        ConfigFuture configFuture = new ConfigFuture(dataId, content, ConfigOperation.PUTIFABSENT, timeoutMills);
        configOperateExecutor.submit(new ConfigOperateRunnable(configFuture));
        return (Boolean)configFuture.get();
    }

    @Override
    public boolean putConfigIfAbsent(String dataId, String content) {
        return putConfigIfAbsent(dataId, content, DEFAULT_CONFIG_TIMEOUT);
    }

    @Override
    public boolean removeConfig(String dataId, long timeoutMills) {
        ConfigFuture configFuture = new ConfigFuture(dataId, null, ConfigFuture.ConfigOperation.REMOVE, timeoutMills);
        configOperateExecutor.submit(new ConfigOperateRunnable(configFuture));
        return (Boolean)configFuture.get();
    }

    @Override
    public boolean removeConfig(String dataId) {
        return removeConfig(dataId, DEFAULT_CONFIG_TIMEOUT);
    }

    @Override
    public void addConfigListener(String dataId, ConfigChangeListener listener) {

    }

    @Override
    public void removeConfigListener(String dataId, ConfigChangeListener listener) {

    }

    /**
     * The type Config operate runnable.
     */
    class ConfigOperateRunnable implements Runnable {

        private ConfigFuture configFuture;

        /**
         * Instantiates a new Config operate runnable.
         *
         * @param configFuture the config future
         */
        public ConfigOperateRunnable(ConfigFuture configFuture) {
            this.configFuture = configFuture;
        }

        @Override
        public void run() {
            if (null != configFuture) {
                if (configFuture.isTimeout()) {
                    setFailResult(configFuture);
                }
                if (configFuture.getOperation() == ConfigOperation.GET) {
                    String result = CONFIG.getString(configFuture.getDataId());
                    configFuture.setResult(result == null ? configFuture.getContent() : result);
                } else if (configFuture.getOperation() == ConfigOperation.PUT) {
                    //todo
                    configFuture.setResult(Boolean.TRUE);
                } else if (configFuture.getOperation() == ConfigOperation.PUTIFABSENT) {
                    //todo
                    configFuture.setResult(Boolean.TRUE);
                } else if (configFuture.getOperation() == ConfigOperation.REMOVE) {
                    //todo
                    configFuture.setResult(Boolean.TRUE);
                }
            }
        }

        private void setFailResult(ConfigFuture configFuture) {
            if (configFuture.getOperation() == ConfigOperation.GET) {
                String result = configFuture.getContent();
                configFuture.setResult(result);
            } else {
                configFuture.setResult(Boolean.FALSE);
            }
        }

    }
}
