package io.spring2go.seata.config;

public class ConfigurationFactory {
    private static final Configuration FILE_INSTANCE = new FileConfiguration();

    public static Configuration getInstance() {
        return FILE_INSTANCE;
    }
}
