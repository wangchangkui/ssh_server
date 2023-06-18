package com.coderwang.connect;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ClientChannel;
import org.apache.sshd.client.session.ClientSession;

import java.io.IOException;
import java.util.Optional;

/**
 * @author wck
 * @version 1.0.0
 * @Description
 * @createTime 2023年06月18日 21:55:00
 */
@Slf4j
@Data
public class ClientEntity{


    private SshClient sshClient;

    private ClientSession session;

    private ClientChannel channel;


    /**
     * 关闭连接
     */
    public void close(){
        closeChannel();
        closeSession();
        closeClient();
    }

    /**
     * 关闭连接
     */
    private void closeChannel() {
        if(Optional.ofNullable(channel).isPresent()){
            try {
                channel.close();
            } catch (IOException e) {
                log.error("关闭通道失败");
                e.printStackTrace();
            }
        }
    }


    /**
     * 关闭会话记录
     */
    private void closeSession() {
        if(Optional.ofNullable(session).isPresent()){
            try {
                session.close();
            } catch (IOException e) {
                log.error("关闭session失败");
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭连接
     */
    private void closeClient() {
        if(Optional.ofNullable(sshClient).isPresent()){
            try {
                sshClient.close();
            } catch (IOException e) {
                log.error("关闭客户端失败");
                e.printStackTrace();
            }
        }
    }
}
