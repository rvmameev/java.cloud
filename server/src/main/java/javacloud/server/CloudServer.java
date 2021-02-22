package javacloud.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import javacloud.server.handlers.CommandHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class CloudServer {
    private final ServerConfig serverConfig;

    public CloudServer(ServerConfig serverConfig) {
        this.serverConfig = Objects.requireNonNull(serverConfig);
    }

    public void run()
    {
        EventLoopGroup worker = new NioEventLoopGroup();

        try {

            Path dataPath = Paths.get(serverConfig.getServerDataDirectory());

            if (!Files.exists(dataPath)) {
                Files.createDirectory(dataPath);
            }

            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline().addLast(
                            new ObjectEncoder(),
                            new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                            new CommandHandler(CloudServer.this)
                        );
                    }
                });

            ChannelFuture future = bootstrap.bind(serverConfig.getPort()).sync();

            System.out.println("Server started on " + future.channel().localAddress());

            future.channel().closeFuture().sync();
        } catch (InterruptedException | IOException e) {
            System.out.println("Server was broken");
        } finally {
            worker.shutdownGracefully();
        }
    }

    public ServerConfig getServerConfig() {
        return serverConfig;
    }
}