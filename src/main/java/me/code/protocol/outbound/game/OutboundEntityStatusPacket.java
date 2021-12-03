package me.code.protocol.outbound.game;

import io.netty.buffer.ByteBuf;
import me.code.protocol.OutboundPacket;

public class OutboundEntityStatusPacket extends OutboundPacket {

    private int entityId;
    private byte status;

    public OutboundEntityStatusPacket(int entityId, byte status) {
        super(0x1B);
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeInt( entityId );
        buf.writeByte( status );
    }
}
