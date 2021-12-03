package me.code.protocol.outbound.login;

import io.netty.buffer.ByteBuf;
import me.code.protocol.OutboundPacket;
import me.code.util.BufferUtil;

public class OutboundLoginPacket extends OutboundPacket {

    private String username;

    public OutboundLoginPacket(String username) {
        super(0x00);

        this.username = username;
    }

    @Override
    public void write(ByteBuf buf) {
        BufferUtil.writeVarString(username, buf);
    }
}
