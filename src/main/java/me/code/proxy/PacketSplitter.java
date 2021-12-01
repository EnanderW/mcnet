package me.code.proxy;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import me.code.util.BufferUtil;

import java.util.List;

public class PacketSplitter extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf buf, List<Object> list) throws Exception {
        while (buf.readableBytes() > 0) {
            buf.markReaderIndex();
            int length = BufferUtil.readVarInt(buf);

            buf.resetReaderIndex();

            ByteBuf outBuf = Unpooled.buffer(length + BufferUtil.getVarIntSize(length));
            buf.readBytes(outBuf, length + BufferUtil.getVarIntSize(length));

            list.add(outBuf);
        }
    }

}
