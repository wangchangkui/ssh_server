package com.coderwang.connect;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import org.apache.sshd.client.channel.ClientChannel;
import org.apache.sshd.client.channel.ClientChannelEvent;
import org.apache.sshd.client.future.OpenFuture;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.channel.Channel;
import org.apache.sshd.common.future.CloseFuture;
import org.apache.sshd.common.future.SshFutureListener;
import org.apache.sshd.common.util.io.input.NoCloseInputStream;

/**
 * @Title: NettySshClientHandler
 * @Author coderWang
 * @Date 2023/6/16 22:15
 * @description: ssh
 */
public class NettySshClientHandler extends ChannelInboundHandlerAdapter {
    public static final AttributeKey<ClientSession> SESSION_KEY = AttributeKey.valueOf("session");

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        ClientSession session = ctx.channel().attr(SESSION_KEY).get();
        if (session != null && session.isAuthenticated()) {
            // 在SSH会话上执行命令
            ClientChannel channel = session.createChannel(Channel.CHANNEL_SHELL);
            channel.setIn(new NoCloseInputStream(System.in));
            channel.setOut(System.out);
            channel.setErr(System.err);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf =(ByteBuf) msg;
        System.out.println("收到服务端" + ctx.channel().remoteAddress() + "的消息：" + byteBuf.toString(CharsetUtil.UTF_8));
        super.channelRead(ctx, msg);
    }
}
