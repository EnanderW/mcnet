package me.code.protocol;

import lombok.Getter;
import me.code.protocol.inbound.game.InboundJoinGamePacket;
import me.code.protocol.inbound.handshake.InboundIntentionPacket;
import me.code.protocol.inbound.login.InboundDisconnectPacket;
import me.code.protocol.inbound.login.InboundLoginPacket;
import me.code.protocol.inbound.login.InboundLoginSuccessPacket;
import me.code.protocol.inbound.status.InboundStatusPingPacket;
import me.code.protocol.inbound.status.InboundStatusRequestPacket;

@Getter
public enum PacketState {

    CLIENT_HANDSHAKE(new Class[]{ InboundIntentionPacket.class }),
    CLIENT_STATUS(new Class[]{ InboundStatusRequestPacket.class, InboundStatusPingPacket.class }),
    CLIENT_LOGIN(new Class[]{ InboundLoginPacket.class }),
    CLIENT_GAME(new Class[]{ }),

    SERVER_LOGIN(new Class[]{ InboundDisconnectPacket.class, null, InboundLoginSuccessPacket.class }),
    SERVER_GAME(new Class[]{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, InboundJoinGamePacket.class });


    private final Class<? extends InboundPacket>[] inboundPackets;

    PacketState(Class<? extends InboundPacket>[] inboundPackets) {
        this.inboundPackets = inboundPackets;
    }

    public Class<? extends InboundPacket> getPacket(int packetId) {
        return (packetId < 0 || packetId >= inboundPackets.length) ? null : inboundPackets[packetId];
    }
}
