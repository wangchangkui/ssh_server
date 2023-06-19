package com.coderwang.config;

import com.coderwang.antation.PropertyReadAnnotation;
import com.coderwang.exception.ReadConfigException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;
import java.util.stream.Stream;

public class CustomerReadConfig implements ReadConfig{


    private volatile ConnectConfig connectConfig;

    /**
     * 这个位置是基于resource路径的地址
     */
    private  String configPath = "config.properties";




    public CustomerReadConfig(String configPath){
        this.configPath = configPath;

    }


    public CustomerReadConfig(){

    }


    /**
     * 获取链接
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
        // 从resource 目录下创建
        try (InputStream ins = this.getClass().getClassLoader().getResourceAsStream(configPath)) {
           return setConnectConfig(ins);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ReadConfigException("无法读取指定的配置文件的连接信息");
        }
    }


    /**
     * 设置链接
     * @param ins 输入刘
     * @return 链接对象
     * @throws IOException 异常
     */
    public ConnectConfig setConnectConfig(InputStream ins) throws IOException {
        if(ins == null){
            throw new ReadConfigException("输入流为空，无法读取");
        }
        Properties properties = null;
        try {
            properties = new Properties();
            properties.load(ins);
        } catch (IOException e) {
            throw new ReadConfigException("也许是一个无效的输入流。");
        }
        ConnectConfig connectConfig = new ConnectConfig();
        // 使用反射的方式加载配置文件，下次只需要注解上的值。
        Field[] declaredFields = ConnectConfig.class.getDeclaredFields();
        Properties finalProperties = properties;
        Stream.of(declaredFields).forEach(d->{
            d.setAccessible(true);
            PropertyReadAnnotation annotation = d.getAnnotation(PropertyReadAnnotation.class);
            Object value = finalProperties.get(annotation.value());
            try {
                d.set(connectConfig,value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
        this.connectConfig = connectConfig;
        return connectConfig;
    }


}
