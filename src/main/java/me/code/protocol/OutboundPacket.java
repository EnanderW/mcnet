package me.code.protocol;

import io.netty.buffer.ByteBuf;
import lombok.Getter;

@Getter
public abstract class OutboundPacket {

    private final int packetId;

    public OutboundPacket(int packetId) {
        this.packetId = packetId;
    }

    public abstract void write(ByteBuf buf);
}
