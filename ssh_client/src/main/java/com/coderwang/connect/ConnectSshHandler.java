package com.coderwang.connect;

import com.coderwang.config.ConnectConfig;
import com.coderwang.config.ReadConfig;
import com.coderwang.exception.ConnectException;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ClientChannel;
import org.apache.sshd.client.future.ConnectFuture;
import org.apache.sshd.client.session.ClientSession;

import java.io.IOException;

/**
 * @author 29059
 */
@Slf4j
public class ConnectSshHandler {

    private  ReadConfig readConfig;


    public void setReadConfig(ReadConfig readConfig) {
        this.readConfig = readConfig;
    }

    public ConnectSshHandler(ReadConfig readConfig) {
        this.readConfig = readConfig;
    }

    public ConnectSshHandler() {
    }

    /**
     * 通过配置文件连接 请确保配置文件读取器已经存在
     * @param manager 存储管理器
     * @return 连接实例
     */
    public ClientEntity connectSsh(ClientManagerI manager) {
        ConnectConfig connectConfig = readConfig.readConfig();
        return connectSsh(connectConfig,manager);
    }

    /**
     * 通过参数直接连接
     * @param connectConfig 连接参数
     * @param manager 存储管理器
     * @return 连接实例
     */
    public ClientEntity connectSsh(ConnectConfig connectConfig,ClientManagerI manager){
        if(manager.hasClient(connectConfig.getHost())){
            log.info("连接已经存在,请勿重复连接");
            return null;
        }
        SshClient client = SshClient.setUpDefaultClient();
        client.start();
        ClientSession session;
        ClientChannel channel;
        try {
            // 连接SSH服务器
            ConnectFuture connectFuture = client.connect(connectConfig.getUserName(), connectConfig.getHost(), connectConfig.getPort());
            connectFuture.await();
            session = connectFuture.getSession();
            channel = session.createChannel(ClientChannel.CHANNEL_SHELL);
            session.addPasswordIdentity(connectConfig.getPassWord());
            session.auth().verify();
            // 打开SSH通道
            channel.open().await();
        } catch (IOException e) {
            throw new ConnectException("连接服务器失败:"+e.getMessage());
        }
        log.info("连接服务器成功");
        ClientEntity build = ClientEntity.builder()
                .sshClient(client)
                .session(session)
                .channel(channel)
                .defaultTime(connectConfig.getTimeOut())
                .build();
        manager.addClient(connectConfig.getHost(), build);
        return build;
    }

}
