package com.coderwang.config;

/**
 * @author wck
 * @version 1.0.0
 * @Description
 * @createTime 2023年07月06日 11:04:00
 */
public class ConnectConfigFactory {

    public static ReadConfig getReadConfig(ConfigType configType) {
        return switch (configType) {
            case YAML -> new YamlReadConfig();
            case PROPERTIES -> new PropertiesReadConfig();
        };
    }
}
