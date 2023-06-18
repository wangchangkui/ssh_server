package com.coderwang.connect;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ClientChannel;
import org.apache.sshd.client.channel.ClientChannelEvent;
import org.apache.sshd.client.session.ClientSession;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import java.util.List;

/**
 * @author wck
 * @version 1.0.0
 * @Description
 * @createTime 2023年06月18日 21:55:00
 */
@Slf4j
@Data
@Builder
public class ClientEntity{


    private SshClient sshClient;

    private ClientSession session;

    private ClientChannel channel;



    public void writeCmd(String cmd, long writeTime){
        if(channel == null){
            log.warn("通道不存在,请先连接服务器后再次执行");
            return;
        }
        // 发送命令并等待响应
        String commandOutput;
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            channel.setOut(output);
            channel.setErr(output);
            channel.getInvertedIn().write(cmd.getBytes());
            channel.getInvertedIn().flush();
            channel.waitFor(List.of(ClientChannelEvent.STDOUT_DATA), writeTime);
            //获取命令输出
            commandOutput = output.toString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Command output: " + commandOutput);
    }

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
