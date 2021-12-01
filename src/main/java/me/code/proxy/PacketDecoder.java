package me.code.proxy;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.Setter;
import me.code.protocol.InboundPacket;
import me.code.protocol.PacketState;
import me.code.util.BufferUtil;

import java.util.List;

public class PacketDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Setter
    private PacketState state;

    public PacketDecoder() {
        this.state = PacketState.HANDSHAKE;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> list) throws Exception {
        int length = BufferUtil.readVarInt(buf);;
        int packetId = BufferUtil.readVarInt(buf);

        Class<? extends InboundPacket> packetClass = state.getPacket(packetId);
        if (packetClass == null) {
            ctx.close();
            return;
        }

        InboundPacket packet = (InboundPacket) packetClass.getConstructors()[0].newInstance();
        packet.read(buf);

        list.add(packet);
    }

}
