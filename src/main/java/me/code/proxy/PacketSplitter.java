package me.code.proxy;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import me.code.util.BufferUtil;

import java.util.List;

public class PacketSplitter extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        // If we decode an invalid packet and an exception is thrown (thus triggering a close of the connection),
        // the Netty ByteToMessageDecoder will continue to frame more packets and potentially call fireChannelRead()
        // on them, likely with more invalid packets. Therefore, check if the connection is no longer active and if so
        // sliently discard the packet.
        if ( !ctx.channel().isActive() )
        {
            in.skipBytes( in.readableBytes() );
            return;
        }

        in.markReaderIndex();

        final byte[] buf = new byte[ 3 ];
        for ( int i = 0; i < buf.length; i++ )
        {
            if ( !in.isReadable() )
            {
                in.resetReaderIndex();
                return;
            }

            buf[i] = in.readByte();
            if ( buf[i] >= 0 )
            {
                int length = BufferUtil.readVarInt( Unpooled.wrappedBuffer( buf ) );
                if ( length == 0 )
                {
                    throw new CorruptedFrameException( "Empty Packet!" );
                }

                if ( in.readableBytes() < length )
                {
                    in.resetReaderIndex();
                    return;
                } else
                {
                    if ( in.hasMemoryAddress() )
                    {
                        out.add( in.slice( in.readerIndex(), length ).retain() );
                        in.skipBytes( length );
                    } else
                    {
                        ByteBuf dst = ctx.alloc().directBuffer( length );
                        in.readBytes( dst );
                        out.add( dst );
                    }
                    return;
                }
            }
        }

        throw new CorruptedFrameException( "length wider than 21-bit" );

//        while (buf.readableBytes() > 0) {
//
//            buf.markReaderIndex();
//            int length = BufferUtil.readVarInt(buf);
////            System.out.println("Length: " + length);
//
//            buf.resetReaderIndex();
//
//            ByteBuf outBuf = Unpooled.buffer(length + BufferUtil.getVarIntSize(length));
//            buf.readBytes(outBuf, length + BufferUtil.getVarIntSize(length));
//
//            list.add(outBuf);
//        }
    }

}
