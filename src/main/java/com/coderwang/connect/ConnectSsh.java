package com.coderwang.connect;

import com.coderwang.config.ConnectConfig;
import com.coderwang.config.ReadConfig;
import com.coderwang.exception.ConnectException;
import com.coderwang.handler.MySshClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.future.AuthFuture;
import org.apache.sshd.client.future.ConnectFuture;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.PropertyResolverUtils;

import java.net.InetSocketAddress;
import java.util.Collections;

/**
 * @author 29059
 */
public class ConnectSsh {

    private final ReadConfig readConfig;

    private  Channel channel;

    private final EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

    public ConnectSsh(ReadConfig readConfig) {
        this.readConfig = readConfig;
    }


    /**
     * 连接到一个ssh 服务器
     */
    public void connectSsh(){
        ConnectConfig connectConfig = readConfig.readConfig();
        try{
            Bootstrap bootstrap =  new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) {
                            //添加客户端通道的处理器
                            ch.pipeline().addLast(new NettySshClientHandler());
                        }
                    });
            SshClient sshClient = SshClient.setUpDefaultClient();
            sshClient.start();
            ConnectFuture connectFuture =sshClient
                    .connect(connectConfig.getUserName(), connectConfig.getHost(), connectConfig.getPort())
                    .verify();
            connectFuture.await();
            ClientSession session = connectFuture.getSession();
            session.addPasswordIdentity(connectConfig.getPassWord());
            AuthFuture verify = session.auth().verify();
            verify.await();


            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress(connectConfig.getHost(), connectConfig.getPort())).sync();
            Channel channel = channelFuture.channel();
            // 将SSH会话绑定到Netty通道
            channel.attr(NettySshClientHandler.SESSION_KEY).set(session);
            // 等待连接关闭
            channel.closeFuture().sync();
        }catch (Exception e){
            throw new ConnectException("连接服务器失败了",e);
        }
    }


    /**
     * 关闭连接
     */
    public void disConnect(){
        if(channel != null && channel.isOpen()){
            channel.flush();
            try {
                channel.closeFuture().sync();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
