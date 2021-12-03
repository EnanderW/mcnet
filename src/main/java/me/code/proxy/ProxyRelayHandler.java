package me.code.proxy;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ProxyRelayHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private Channel relayChannel;

    public ProxyRelayHandler(Channel relayChannel) {
        this.relayChannel = relayChannel;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        ByteBuf out = Unpooled.copiedBuffer(byteBuf);
        relayChannel.writeAndFlush(out);
    }
}
