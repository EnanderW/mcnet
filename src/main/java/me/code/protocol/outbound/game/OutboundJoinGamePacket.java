package me.code.protocol.outbound.game;

import io.netty.buffer.ByteBuf;
import me.code.protocol.OutboundPacket;
import me.code.protocol.inbound.game.InboundJoinGamePacket;
import me.code.util.BufferUtil;
import se.llbit.nbt.Tag;

public class OutboundJoinGamePacket extends OutboundPacket {

    private InboundJoinGamePacket packet;

    public OutboundJoinGamePacket(InboundJoinGamePacket packet) {
        super(0x26);

        this.packet = packet;
    }

    @Override
    public void write(ByteBuf buf) {
        buf.writeInt( packet.getEntityId() );
        buf.writeBoolean( packet.isHardcore() );
        buf.writeByte( packet.getGameMode() );
        buf.writeByte( packet.getPreviousGameMode() );

        BufferUtil.writeVarInt(packet.getWorldNames().size(), buf);
        for (String world : packet.getWorldNames())
                BufferUtil.writeVarString(world, buf);


        BufferUtil.writeTag( packet.getDimensions(), buf );

        BufferUtil.writeTag( (Tag) packet.getDimension(), buf );
        BufferUtil.writeVarString( packet.getWorldName(), buf );
        buf.writeLong( packet.getSeed() );
        BufferUtil.writeVarInt( packet.getMaxPlayers(), buf );

        BufferUtil.writeVarInt( packet.getViewDistance(), buf );
        buf.writeBoolean( packet.isReducedDebugInfo() );
        buf.writeBoolean( packet.isNormalRespawn() );
        buf.writeBoolean( packet.isDebug() );
        buf.writeBoolean( packet.isFlat() );
    }
}
