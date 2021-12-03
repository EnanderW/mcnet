package me.code.proxy;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import me.code.ServerInfo;
import me.code.config.ServerConfig;
import me.code.protocol.PacketState;
import me.code.protocol.outbound.handshake.OutboundIntentionPacket;
import me.code.protocol.outbound.login.OutboundLoginPacket;

import java.util.HashMap;
import java.util.Map;

public class Proxy {

    public static Proxy INSTANCE = new Proxy();

    @Getter
    private final ServerConfig config;
    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workerGroup;

    @Getter
    private final EventLoopGroup clientGroup;

    private final Map<String, Connection> connections;

    public Proxy() {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        clientGroup = new NioEventLoopGroup();
        this.connections = new HashMap<>();
        config = new ServerConfig();
    }

    public void start() {
        config.load();

        ChannelInitializer<SocketChannel> initializer = new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline pipeline = socketChannel.pipeline();

                pipeline.addLast(new PacketPrepender());
                pipeline.addLast(new PacketEncoder());

                pipeline.addLast(new PacketSplitter());
                pipeline.addLast(new PacketDecoder());
                pipeline.addLast(new ProxyClientHandler());
            }
        };

        ServerBootstrap bootstrap = new ServerBootstrap();

        try {
            bootstrap
                    .channel(NioServerSocketChannel.class)
                    .group(bossGroup, workerGroup)
                    .childHandler(initializer)
                    .bind(config.getPort()).sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    public void save(Connection connection) {
        this.connections.put(connection.getUsername(), connection);
    }

    public void remove(Connection connection) {
        this.connections.remove(connection.getUsername());
    }

    public Connection getConnection(String username) {
        return this.connections.get(username);
    }

    public static void connect(ServerInfo info, Connection connection) {
        ChannelInitializer<SocketChannel> initializer = new ChannelInitializer<>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline pipeline = socketChannel.pipeline();

                pipeline.addLast(new PacketPrepender());
                pipeline.addLast(new PacketEncoder());

                pipeline.addLast(new PacketSplitter());
                pipeline.addLast(new PacketDecoder(PacketState.SERVER_LOGIN));
                pipeline.addLast(new ProxyServerHandler(connection));
            }
        };

        Bootstrap bootstrap = new Bootstrap();

        try {
            Channel channel = bootstrap.group(Proxy.INSTANCE.getClientGroup())
                    .channel(NioSocketChannel.class)
                    .handler(initializer)
                    .connect(info.getAddress(), info.getPort())
                    .sync().channel();

            channel.writeAndFlush(new OutboundIntentionPacket());
            channel.writeAndFlush(new OutboundLoginPacket(connection.getUsername()));

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
