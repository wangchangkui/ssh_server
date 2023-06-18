package com.coderwang.connect;

import com.coderwang.config.ConnectConfig;
import com.coderwang.config.ReadConfig;
import com.coderwang.exception.ConnectException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ClientChannel;
import org.apache.sshd.client.channel.ClientChannelEvent;
import org.apache.sshd.client.session.ClientSession;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;

/**
 * @author 29059
 */
@Slf4j
public class ConnectSsh {

    private final ReadConfig readConfig;


    public ConnectSsh(ReadConfig readConfig) {
        this.readConfig = readConfig;
    }


    /**
     * 连接到一个ssh 服务器
     */
    public void connectSsh(){
        ConnectConfig connectConfig = readConfig.readConfig();
        try{
            sshConnect(connectConfig);
        }catch (Exception e){
            e.printStackTrace();
            throw new ConnectException("连接服务器失败了",e);
        }
    }


    public void sshConnect(ConnectConfig connectConfig){
        SshClient sshClient = SshClient.setUpDefaultClient();
        sshClient.setServerKeyVerifier((clientSession, sshPublicKey, socketAddress) -> true);
        sshClient.setPasswordIdentityProvider(sessionContext -> List.of(connectConfig.getPassWord()));
        sshClient.start();
        ClientSession session = null;
        try {
            session = sshClient
                    .connect(connectConfig.getUserName(),connectConfig.getHost(),connectConfig.getPort())
                    .verify()
                    .getSession();
            session.addPasswordIdentity(connectConfig.getPassWord());
            if (session.isOpen()) {
                log.info("连接服务器成功");
            }
            ClientChannel shell = session.createChannel("shell");
            shell.open().verify();
            shell.waitFor(EnumSet.of(ClientChannelEvent.CLOSED), 0L);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
