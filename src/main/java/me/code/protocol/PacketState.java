package me.code.protocol;

import lombok.Getter;
import me.code.protocol.inbound.handshake.InboundIntentionPacket;
import me.code.protocol.inbound.login.InboundLoginPacket;
import me.code.protocol.inbound.status.InboundStatusPingPacket;
import me.code.protocol.inbound.status.InboundStatusRequestPacket;

@Getter
public enum PacketState {

    HANDSHAKE(new Class[]{ InboundIntentionPacket.class }),
    STATUS(new Class[]{ InboundStatusRequestPacket.class, InboundStatusPingPacket.class }),
    LOGIN(new Class[]{ InboundLoginPacket.class }),
    GAME(new Class[]{ });


    private final Class<? extends InboundPacket>[] inboundPackets;
//    private Class<? extends InboundPacket> outboundPackets[];

    PacketState(Class<? extends InboundPacket>[] inboundPackets) {
        this.inboundPackets = inboundPackets;
    }

    public Class<? extends InboundPacket> getPacket(int packetId) {
        return (packetId < 0 || packetId >= inboundPackets.length) ? null : inboundPackets[packetId];
    }

}
