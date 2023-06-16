package com.coderwang.config;

import cn.hutool.core.util.StrUtil;
import com.coderwang.antation.PropertyReadAnnotation;
import com.coderwang.exception.ReadConfigException;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

public class YamlReadConfig implements ReadConfig{

    String[] defaultFileName = {"application.yaml","application.yml","config.yaml","config.yml"};

    private String useFile;

    private volatile ConnectConfig connectConfig;


    public YamlReadConfig(String useFile) {
        this.useFile = useFile;
    }

    public YamlReadConfig() {
    }


    /**
     * 获取一个连接对象
     * @return 连接对象
     */
    public ConnectConfig getConnectConfig(){
        if(connectConfig == null){
            synchronized (this){
                if (connectConfig == null){
                    return readConfig();
                }
            }
        }
        return connectConfig;
    }

    @Override
    public ConnectConfig readConfig() {
        if(connectConfig != null){
            return connectConfig;
        }
        if(StrUtil.isNotBlank(useFile)){
            try (InputStream ins = this.getClass().getClassLoader().getResourceAsStream(useFile)) {
               return read(ins);
            } catch (IOException e) {
                throw new ReadConfigException("无法从提供的文件路径中读取yaml");
            }
        }else{
            // 加载默认的配置文件
            for (String file : defaultFileName) {
                try (InputStream ins = this.getClass().getClassLoader().getResourceAsStream(file)) {
                    if(ins == null){
                        continue;
                    }
                    ConnectConfig read = read(ins);
                    if(read == null){
                        continue;
                    }
                    this.connectConfig = read;
                    return read;
                } catch (IOException ignored) {
                }
            }
            throw new ReadConfigException("resource 目录下没有连接对象，无法获取连接配置文件");
        }
    }


    public ConnectConfig read(InputStream ins){
        if (ins == null) {
            throw new ReadConfigException("空的流文件");
        }
        ConnectConfig connectConfig = new ConnectConfig();
        Yaml yaml = new Yaml();
        LinkedHashMap<String, Object> configMap = yaml.load(ins);

        Field[] declaredFields = connectConfig.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            PropertyReadAnnotation annotation = field.getAnnotation(PropertyReadAnnotation.class);
            String fieldName = field.getName();
            String propertyKey = annotation.value();
            String[] split = propertyKey.split("\\.");
            // 未设置值 也许是这个配置文件下面没有 继续获取下一个配置文件
            if (!configMap.containsKey(split[0])) {
                return null;
            }

            @SuppressWarnings("unchecked")
            LinkedHashMap<String, Object> fieldValueMap = (LinkedHashMap<String, Object>) configMap.get(split[0]);
            Object fieldValue = fieldValueMap.get(split[1]);

            if (fieldValue == null) {
                throw new ReadConfigException("参数未设置值：" + propertyKey + "." + fieldName);
            }

            try {
                Object convertedValue = convertValue(fieldValue.toString(), field.getType());
                field.set(connectConfig, convertedValue);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("无法设置属性值：" + field.getName(), e);
            }
        }

        return connectConfig;
    }


    /**
     * 转换一个默认的参数
     * @param value 参数值
     * @param targetType 类型
     * @return 转换后的结果对象
     */
    private static Object convertValue(String value, Class<?> targetType) {
        if (targetType == String.class) {
            return value;
        } else if (targetType == Integer.class || targetType == int.class) {
            return Integer.parseInt(value);
        } else if (targetType == Boolean.class || targetType == boolean.class) {
            return Boolean.parseBoolean(value);
        }
        // 可以根据需要添加更多类型的转换

        return null;
    }

}
