package com.coderwang.connect;

/**
 * @author wck
 * @version 1.0.0
 * @Description
 * @createTime 2023年06月18日 21:55:00
 */
public interface ClientManagerI {

    /**
     * 获取一个连接对象
     * @param key 连接对象的key
     * @return 连接对象
     */
    ClientEntity getClient(Object key);

    /**
     * 添加一个连接镀锡
     * @param key 连接对象的key
     * @param clientEntity 连接对象
     */
    void addClient(Object key, ClientEntity clientEntity);

    /**
     * 移除一个连接对象
     * @param key 连接对象的key
     */
    void removeClient(Object key);




}