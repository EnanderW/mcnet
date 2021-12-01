package me.code.protocol.outbound.status;

import io.netty.buffer.ByteBuf;
import me.code.protocol.OutboundPacket;

public class OutboundStatusPingPacket extends OutboundPacket {

    private long time;

    public OutboundStatusPingPacket(long time) {
        super(0x01);

        this.time = time;
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeLong(this.time);
    }
}
