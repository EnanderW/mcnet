package me.code.proxy;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Getter;
import me.code.config.ServerConfig;

public class Proxy {

    public static Proxy INSTANCE = new Proxy();

    @Getter
    private final ServerConfig config;
    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workerGroup;

    public Proxy() {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
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

}
