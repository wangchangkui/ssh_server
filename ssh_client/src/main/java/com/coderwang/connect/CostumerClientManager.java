package com.coderwang.connect;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wck
 * @version 1.0.0
 * @Description
 * @createTime 2023年06月18日 21:57:00
 */
@Slf4j
public class CostumerClientManager implements ClientManagerI {

    private CostumerClientManager() {
    }

    /**
     * 客户端连接实例
     */
    private final ConcurrentHashMap<String,ClientEntity> CLIENT_ENTITY_MAP = new ConcurrentHashMap<>();


    /**
     * 单例 唯一
     */
    private static final CostumerClientManager CLIENT_MANAGER = new CostumerClientManager();


    /**
     * 查询连接是否已经存在
     * @param key 连接的key
     * @return 是否存在
     */
    @Override
    public boolean hasClient(String key){
        return CLIENT_ENTITY_MAP.containsKey(key);
    }


    /**
     * 获取一个连接实例
     * @return 连接实例
     */
    public static CostumerClientManager getInstance(){
        return CLIENT_MANAGER;
    }


    @Override
    public ClientEntity getClient(Object key) {
        if(CLIENT_ENTITY_MAP.containsKey(key.toString())){
            return CLIENT_ENTITY_MAP.get(key.toString());
        }
        return null;
    }

    @Override
    public void addClient(Object key, ClientEntity clientEntity) {
        CLIENT_ENTITY_MAP.put(key.toString(),clientEntity);
    }

    @Override
    public void removeClient(Object key) {
        ClientEntity client = getClient(key);
        if(client == null){
            log.warn("客户端不存在");
            return;
        }
        client.close();
    }
}
