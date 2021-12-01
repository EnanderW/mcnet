package me.code.protocol.inbound.status;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import me.code.protocol.InboundPacket;
import me.code.protocol.OutboundPacket;
import me.code.protocol.outbound.status.OutboundStatusPingPacket;

public class InboundStatusPingPacket implements InboundPacket {

    private long time;

    @Override
    public void read(ByteBuf buf) {
        this.time = buf.readLong();
    }

    @Override
    public void handle(ChannelHandlerContext ctx) {
        OutboundPacket packet = new OutboundStatusPingPacket(time);
        ctx.channel().writeAndFlush(packet);
    }
}
