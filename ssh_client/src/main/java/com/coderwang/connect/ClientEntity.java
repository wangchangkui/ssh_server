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
import java.util.List;
import java.util.Optional;

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

    private Long defaultTime;


    private final List<ClientChannelEvent> list= List.of(ClientChannelEvent.STDOUT_DATA, ClientChannelEvent.STDERR_DATA);
    /**
     * 执行命令 然后返回结果给前端
     * @param cmd 命令
     * @return 内容
     */
    public String writeCmd(String cmd){
        if(channel == null){
            log.warn("通道不存在,请先连接服务器后再次执行");
            return "";
        }
        // 发送命令并等待响应
        String commandOutput;
        // 这里不能设置为静态变量，如果使用静态变量，那么每次执行命令都会使用同一个ByteArrayOutputStream 里面内容无限叠加
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()){
            channel.setOut(output);
            channel.setErr(output);
            channel.getInvertedIn().write(cmd.getBytes());
            channel.getInvertedIn().flush();
            channel.waitFor(list, defaultTime);
            //获取命令输出
            String response = output.toString();
            String[] split = response.split(System.lineSeparator());
            // 多判断一次 如果小于 等于 1 则说明没有输出完毕，继续等待
            while (split.length <= 1){
                response = output.toString(StandardCharsets.UTF_8);
                split = response.split(System.lineSeparator());
            }
            commandOutput = response;
        } catch (IOException  e) {
            throw new RuntimeException(e);
        }
        int firstLineEndIndex = commandOutput.indexOf(System.lineSeparator());
        if (firstLineEndIndex != -1) {
            commandOutput = commandOutput.substring(firstLineEndIndex + System.lineSeparator().length());
        }
       return commandOutput;
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
