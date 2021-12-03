package me.code.protocol.outbound.game;

import io.netty.buffer.ByteBuf;
import me.code.protocol.OutboundPacket;
import me.code.protocol.inbound.game.InboundJoinGamePacket;
import me.code.util.BufferUtil;
import se.llbit.nbt.CompoundTag;

public class OutboundRespawnPacket extends OutboundPacket {

    private InboundJoinGamePacket packet;

    public OutboundRespawnPacket(InboundJoinGamePacket packet) {
        super(0x3D);

        this.packet = packet;
    }

    @Override
    public void write(ByteBuf buf) {
        BufferUtil.writeTag(packet.getDimension(), buf);
        BufferUtil.writeVarString(packet.getWorldName(), buf);
        buf.writeLong(packet.getSeed());
        buf.writeByte(packet.getGameMode());
        buf.writeByte(packet.getPreviousGameMode());
        buf.writeBoolean(packet.isDebug());
        buf.writeBoolean(packet.isFlat());
        buf.writeBoolean(false);
    }
}
