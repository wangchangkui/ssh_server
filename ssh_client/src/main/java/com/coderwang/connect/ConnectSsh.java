package com.coderwang.connect;

import com.coderwang.config.ConnectConfig;
import com.coderwang.config.ReadConfig;
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
public class ConnectSsh {

    private final ReadConfig readConfig;


    public ConnectSsh(ReadConfig readConfig) {
        this.readConfig = readConfig;
    }


    public void connectSsh() {
        CostumerClientManager manager = CostumerClientManager.getInstance();
        ConnectConfig connectConfig = readConfig.readConfig();
        if(manager.hasClient(connectConfig.getHost())){
            log.info("连接已经存在,请勿重复连接");
            return;
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
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        log.info("连接服务器成功");
        ClientEntity build = ClientEntity.builder()
                .sshClient(client)
                .session(session)
                .channel(channel)
                .defaultTime(200L)
                .build();
        manager.addClient(connectConfig.getHost(), build);
    }

}
