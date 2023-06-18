package com.coderwang.connect;

import com.coderwang.config.ConnectConfig;
import com.coderwang.config.ReadConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ClientChannel;
import org.apache.sshd.client.channel.ClientChannelEvent;
import org.apache.sshd.client.future.ConnectFuture;
import org.apache.sshd.client.session.ClientSession;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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


    public void connectSsh() {
        ConnectConfig connectConfig = readConfig.readConfig();
        SshClient client = SshClient.setUpDefaultClient();
        client.start();
        try {
            // 连接SSH服务器
            ConnectFuture connectFuture = client.connect(connectConfig.getUserName(), connectConfig.getHost(), connectConfig.getPort());
            connectFuture.await();
            ClientSession session = connectFuture.getSession();
            // 创建SSH通道
            try (session; ClientChannel channel = session.createChannel(ClientChannel.CHANNEL_SHELL)) {
                session.addPasswordIdentity(connectConfig.getPassWord());
                session.auth().verify();
                // 打开SSH通道
                channel.open().await();
                log.info("连接服务器成功");
                // 发送命令并等待响应
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                channel.setOut(output);
                channel.setErr(output);
                channel.getInvertedIn().write("ls\n".getBytes());
                channel.getInvertedIn().flush();
                channel.waitFor(List.of(ClientChannelEvent.STDOUT_DATA), 100);
                String commandOutput = output.toString(StandardCharsets.UTF_8);
                System.out.println("Command output: " + commandOutput);
                channel.getInvertedIn().write("ls\n".getBytes());
                channel.getInvertedIn().flush();
                channel.getInvertedIn().close();
                channel.waitFor(List.of(ClientChannelEvent.STDOUT_DATA), 100);
                // 获取命令输出
                commandOutput = output.toString(StandardCharsets.UTF_8);
                System.out.println("Command output: " + commandOutput);
            }
            // 关闭SSH通道
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
