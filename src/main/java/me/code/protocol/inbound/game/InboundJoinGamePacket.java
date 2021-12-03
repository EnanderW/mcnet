package me.code.protocol.inbound.game;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import lombok.Getter;
import me.code.protocol.InboundPacket;
import me.code.protocol.outbound.game.OutboundEntityStatusPacket;
import me.code.protocol.outbound.game.OutboundGameStatePacket;
import me.code.protocol.outbound.game.OutboundJoinGamePacket;
import me.code.protocol.outbound.game.OutboundRespawnPacket;
import me.code.proxy.Connection;
import me.code.proxy.ProxyRelayHandler;
import me.code.proxy.ProxyServerHandler;
import me.code.util.BufferUtil;
import se.llbit.nbt.Tag;

import java.util.HashSet;
import java.util.Set;

@Getter
public class InboundJoinGamePacket implements InboundPacket {

    private int entityId;
    private boolean hardcore;
    private short gameMode;
    private short previousGameMode;
    private Set<String> worldNames;
    private Tag dimensions;
    private Tag dimension;
    private String worldName;
    private long seed;
    private short difficulty;
    private int maxPlayers;
    private String levelType;
    private int viewDistance;
    private int simulationDistance;
    private boolean reducedDebugInfo;
    private boolean normalRespawn;
    private boolean debug;
    private boolean flat;

    @Override
    public void read(ByteBuf buf) {
        System.out.println("test");
        entityId = buf.readInt();
        hardcore = buf.readBoolean();
        gameMode = buf.readUnsignedByte();
        previousGameMode = buf.readByte();

        worldNames = new HashSet<>();
        int worldCount = BufferUtil.readVarInt(buf);
        for (int i = 0; i < worldCount; i++)
            worldNames.add(BufferUtil.readVarString(buf));

        dimensions = (Tag) BufferUtil.readTag(buf);
        dimension = (Tag) BufferUtil.readTag(buf);
        worldName = BufferUtil.readVarString(buf);
        seed = buf.readLong();
        maxPlayers = BufferUtil.readVarInt(buf);
        viewDistance = BufferUtil.readVarInt(buf);
        reducedDebugInfo = buf.readBoolean();
        normalRespawn = buf.readBoolean();
        debug = buf.readBoolean();
        flat = buf.readBoolean();

        System.out.println(entityId);
        worldNames.forEach(System.out::println);
    }

    @Override
    public void handle(ChannelHandlerContext ctx) {
        Connection connection = ctx.pipeline().get(ProxyServerHandler.class).getConnection();
        Channel client = connection.getClientChannel();

//        if ( user.getSettings() != null )
//        {
//            ch.write( user.getSettings() );
//        }

        if ( connection.getServerChannel() == null ) {

            OutboundJoinGamePacket packet = new OutboundJoinGamePacket(this);
            client.writeAndFlush(packet);
        } else {

            // Update debug info from login packet
            client.writeAndFlush(new OutboundEntityStatusPacket(entityId, (byte) 22));
            client.writeAndFlush(new OutboundGameStatePacket((byte) 11, 1));
            client.writeAndFlush(new OutboundRespawnPacket(this));
            client.writeAndFlush(new OutboundRespawnPacket(this));

            connection.getServerChannel().close();
        }
        connection.setServerChannel(ctx.channel());
       startRelay(connection);
    }

    private void startRelay(Connection connection) {
        ChannelPipeline serverPipeline = connection.getServerChannel().pipeline();

        Channel serverChannel = connection.getServerChannel();
        Channel clientChannel = connection.getClientChannel();

        ChannelPipeline clientPipeline = clientChannel.pipeline();

        while (serverPipeline.first() != null)
            serverPipeline.removeFirst();

        while (clientPipeline.first() != null)
            clientPipeline.removeFirst();

        serverPipeline.addLast(new ProxyRelayHandler(clientChannel));
        clientPipeline.addLast(new ProxyRelayHandler(serverChannel));
    }
}
