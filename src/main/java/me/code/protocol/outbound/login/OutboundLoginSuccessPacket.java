package me.code.protocol.outbound.login;

import io.netty.buffer.ByteBuf;
import me.code.protocol.OutboundPacket;
import me.code.util.BufferUtil;

import java.util.UUID;

public class OutboundLoginSuccessPacket extends OutboundPacket{

    private UUID uuid;
    private String username;

    public OutboundLoginSuccessPacket(String username) {
        super(0x02);

        this.username = username;
        this.uuid = UUID.randomUUID();
    }

    @Override
    public void write(ByteBuf buf) {
        BufferUtil.writeUUID(this.uuid, buf);
        BufferUtil.writeVarString(this.username, buf);
    }
}
