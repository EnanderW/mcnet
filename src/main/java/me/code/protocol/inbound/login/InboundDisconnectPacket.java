package me.code.protocol.inbound.login;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import me.code.protocol.InboundPacket;
import me.code.util.BufferUtil;

public class InboundDisconnectPacket implements InboundPacket {

    private String message;

    @Override
    public void read(ByteBuf buf) {
        this.message = BufferUtil.readVarString(buf);
    }

    @Override
    public void handle(ChannelHandlerContext ctx) {
        System.out.println("Disconnect: " + message);
        ctx.close();
    }
}
