package com.coderwang.connect;

/**
 * @author wck
 * @version 1.0.0
 * @Description
 * @createTime 2023年06月18日 21:55:00
 */
public interface ClientManagerI {

    ClientEntity getClient(Object key);

    void addClient(Object key, ClientEntity clientEntity);

    void removeClient(Object key);


}
