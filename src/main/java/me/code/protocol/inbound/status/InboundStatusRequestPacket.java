package me.code.protocol.inbound.status;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import me.code.protocol.InboundPacket;
import me.code.protocol.OutboundPacket;
import me.code.protocol.outbound.status.OutboundStatusResponsePacket;

public class InboundStatusRequestPacket implements InboundPacket {

    @Override
    public void read(ByteBuf buf) {}

    @Override
    public void handle(ChannelHandlerContext ctx) {
        OutboundPacket packet = new OutboundStatusResponsePacket();
        ctx.channel().writeAndFlush(packet);
    }
}
