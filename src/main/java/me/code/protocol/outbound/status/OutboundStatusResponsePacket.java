package me.code.protocol.outbound.status;

import io.netty.buffer.ByteBuf;
import me.code.protocol.OutboundPacket;
import me.code.proxy.Proxy;
import me.code.util.BufferUtil;

public class OutboundStatusResponsePacket extends OutboundPacket {

    public OutboundStatusResponsePacket() {
        super(0x00);
    }

    private final String json = "{ \"version\": { \"name\": \"%s\", \"protocol\": %d }, \"players\": { \"max\": %d, \"online\": %d, \"sample\": [] }, \"description\": { \"text\": \"%s\" }, \"favicon\": \"data:image/png;base64,<data>\" }";

    @Override
    public void write(ByteBuf buf) {

        int maxPlayers = 1;
        int onlinePlayers = 1;
        String description = Proxy.INSTANCE.getConfig().getDescription();

        String result = String.format(json, "1.17.1", 756, maxPlayers, onlinePlayers, description);
        BufferUtil.writeVarString(result, buf);
    }

}
