package me.code.protocol.outbound.handshake;

import io.netty.buffer.ByteBuf;
import me.code.protocol.OutboundPacket;
import me.code.proxy.Proxy;
import me.code.util.BufferUtil;

public class OutboundIntentionPacket extends OutboundPacket {

    public OutboundIntentionPacket() {
        super(0x00);
    }

    @Override
    public void write(ByteBuf buf) {
        BufferUtil.writeVarInt(756, buf);
        BufferUtil.writeVarString("localhost", buf);
        buf.writeShort(Proxy.INSTANCE.getConfig().getPort());
        BufferUtil.writeVarInt(2, buf);
    }
}
