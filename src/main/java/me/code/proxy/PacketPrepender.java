package me.code.proxy;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import me.code.util.BufferUtil;

public class PacketPrepender extends MessageToByteEncoder<ByteBuf> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf buf, ByteBuf outBuf) throws Exception {
        int length = buf.readableBytes();
        BufferUtil.writeVarInt(length, outBuf);
        outBuf.writeBytes(buf);
    }

}
