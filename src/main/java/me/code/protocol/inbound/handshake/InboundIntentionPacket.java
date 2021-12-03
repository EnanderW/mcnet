package me.code.protocol.inbound.handshake;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import lombok.Getter;
import me.code.protocol.InboundPacket;
import me.code.protocol.PacketState;
import me.code.proxy.PacketDecoder;
import me.code.util.BufferUtil;

@Getter
public class InboundIntentionPacket implements InboundPacket {

    private int protocolVersion;
    private String serverAddress;
    private int port;
    private int nextState;

    @Override
    public void read(ByteBuf buf) {
        protocolVersion = BufferUtil.readVarInt(buf);
        serverAddress = BufferUtil.readVarString(buf);
        port = buf.readShort();
        nextState = BufferUtil.readVarInt(buf);
    }

    @Override
    public void handle(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        ChannelPipeline pipeline = channel.pipeline();
        PacketDecoder decoder = pipeline.get(PacketDecoder.class);

        if (nextState == 1)
            decoder.setState(PacketState.CLIENT_STATUS);
        else if (nextState == 2)
            decoder.setState(PacketState.CLIENT_LOGIN);
    }

    @Override
    public String toString() {
        return "InboundIntentionPacket{" +
                "protocolVersion=" + protocolVersion +
                ", serverAddress='" + serverAddress + '\'' +
                ", port=" + port +
                ", nextState=" + nextState +
                '}';
    }
}
