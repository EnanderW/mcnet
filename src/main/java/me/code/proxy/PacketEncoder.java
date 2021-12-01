package me.code.proxy;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import me.code.protocol.OutboundPacket;
import me.code.util.BufferUtil;

public class PacketEncoder extends MessageToByteEncoder<OutboundPacket> {

    @Override
    protected void encode(ChannelHandlerContext ctx, OutboundPacket packet, ByteBuf buf) throws Exception {
        BufferUtil.writeVarInt(packet.getPacketId(), buf);
        packet.write(buf);
    }
}
