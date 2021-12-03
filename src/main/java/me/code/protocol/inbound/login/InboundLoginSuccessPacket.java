package me.code.protocol.inbound.login;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import me.code.protocol.InboundPacket;
import me.code.protocol.PacketState;
import me.code.proxy.PacketDecoder;
import me.code.util.BufferUtil;

public class InboundLoginSuccessPacket implements InboundPacket {

    @Override
    public void read(ByteBuf buf) {
        buf.readLong();
        buf.readLong();
        String username = BufferUtil.readVarString(buf);
        System.out.println(username);
    }

    @Override
    public void handle(ChannelHandlerContext ctx) {
        ctx.pipeline().get(PacketDecoder.class).setState(PacketState.SERVER_GAME);
    }
}
