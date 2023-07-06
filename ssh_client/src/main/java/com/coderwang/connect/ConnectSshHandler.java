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

    private final ReadConfig readConfig;



    public ConnectSshHandler(ReadConfig readConfig) {
        this.readConfig = readConfig;
    }


    public ClientEntity connectSsh(ClientManagerI manager) {
        ConnectConfig connectConfig = readConfig.readConfig();
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
                .defaultTime(200L)
                .build();
        manager.addClient(connectConfig.getHost(), build);
        return build;
    }

}
