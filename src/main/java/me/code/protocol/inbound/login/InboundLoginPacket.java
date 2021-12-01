package me.code.protocol.inbound.login;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import me.code.protocol.InboundPacket;
import me.code.protocol.OutboundPacket;
import me.code.protocol.outbound.login.OutboundLoginSuccessPacket;
import me.code.util.BufferUtil;

public class InboundLoginPacket implements InboundPacket {

    private String username;

    @Override
    public void read(ByteBuf buf) {
        this.username = BufferUtil.readVarString(buf);
    }

    @Override
    public void handle(ChannelHandlerContext ctx) {
        OutboundPacket packet = new OutboundLoginSuccessPacket(this.username);
        ctx.channel().writeAndFlush(packet);
    }
}
