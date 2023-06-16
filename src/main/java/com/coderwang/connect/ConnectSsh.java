package com.coderwang.connect;

import com.coderwang.config.ConnectConfig;
import com.coderwang.config.ReadConfig;
import com.coderwang.exception.ConnectException;
import com.coderwang.handler.MySshClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author 29059
 */
public class ConnectSsh {

    private final ReadConfig readConfig;

    private ChannelFuture connect;

    public ConnectSsh(ReadConfig readConfig) {
        this.readConfig = readConfig;
    }


    public void connectSsh(){
        ConnectConfig connectConfig = readConfig.readConfig();
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try{
            Bootstrap bootstrap =  new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) {
                            //添加客户端通道的处理器
                            ch.pipeline().addLast(new MySshClientHandler());
                        }
                    });
            ChannelFuture connect = bootstrap.connect(connectConfig.getHost(), connectConfig.getPort());
            this.connect = connect;
            connect.sync();
        }catch (Exception e){
            throw new ConnectException("连接服务器失败了",e);
        }
    }

}
