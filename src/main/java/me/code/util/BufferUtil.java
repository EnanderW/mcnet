package me.code.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import se.llbit.nbt.NamedTag;
import se.llbit.nbt.Tag;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

public class BufferUtil {

    public static int readVarInt(ByteBuf buf) {
        int i = 0;
        int j = 0;

        byte b0;

        do {
            b0 = buf.readByte();
            i |= (b0 & 127) << j++ * 7;
            if (j > 5) {
                throw new RuntimeException("VarInt too big");
            }
        } while ((b0 & 128) == 128);

        return i;
    }

    public static String readVarString(ByteBuf buf) {
        int length = readVarInt(buf);
        byte[] content = new byte[length];
        buf.readBytes(content);

        return new String(content, 0, length);
    }

    public static Tag readTag(ByteBuf input)
    {
        Tag tag = NamedTag.read( new DataInputStream( new ByteBufInputStream( input ) ) );
        return tag;
    }

    public static void writeVarInt(int i, ByteBuf buf) {
        while ((i & -128) != 0) {
            buf.writeByte(i & 127 | 128);
            i >>>= 7;
        }

        buf.writeByte(i);
    }

    public static void writeVarString(String s, ByteBuf buf) {
        writeVarInt(s.length(), buf);
        buf.writeBytes(s.getBytes());
    }

    public static void writeUUID(UUID value, ByteBuf buf)
    {
        buf.writeLong( value.getMostSignificantBits() );
        buf.writeLong( value.getLeastSignificantBits() );
    }

    public static void writeTag(Tag tag, ByteBuf output)
    {
        try
        {
            tag.write( new DataOutputStream( new ByteBufOutputStream( output ) ) );
        } catch ( IOException ex )
        {
            throw new RuntimeException( "Exception writing tag", ex );
        }
    }

    public static int getVarIntSize(int value) {
        for (int j = 1; j < 5; ++j) {
            if ((value & -1 << j * 7) == 0) {
                return j;
            }
        }

        return 5;
    }
}
