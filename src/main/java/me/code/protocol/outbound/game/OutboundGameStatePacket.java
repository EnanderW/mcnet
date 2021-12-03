package me.code.protocol.outbound.game;

import io.netty.buffer.ByteBuf;
import me.code.protocol.OutboundPacket;

public class OutboundGameStatePacket extends OutboundPacket {

    private byte state;
    private float value;

    public OutboundGameStatePacket(byte state, float value) {
        super(0x1E);
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeByte( state );
        buf.writeFloat( value );
    }
}
