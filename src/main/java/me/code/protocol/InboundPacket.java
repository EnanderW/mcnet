package me.code.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public interface InboundPacket {


    void read(ByteBuf buf);

    void handle(ChannelHandlerContext ctx);
}
