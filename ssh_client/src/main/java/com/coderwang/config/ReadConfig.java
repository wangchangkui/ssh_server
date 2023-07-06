package com.coderwang.config;

/**
 * @author Myxiaowang
 */
public interface ReadConfig {


    /**
     * 读取链接信息
     *
     * @return Connect
     */
    ConnectConfig readConfig();


    /**
     * 转换一个默认的参数
     *
     * @param value      参数值
     * @param targetType 类型
     * @return 转换后的结果对象
     */
    default  Object convertValue(String value, Class<?> targetType) {
        if (targetType == String.class) {
            return value;
        } else if (targetType == Integer.class || targetType == int.class) {
            return Integer.parseInt(value);
        } else if (targetType == Boolean.class || targetType == boolean.class) {
            return Boolean.parseBoolean(value);
        }
        return value;
    }
}
