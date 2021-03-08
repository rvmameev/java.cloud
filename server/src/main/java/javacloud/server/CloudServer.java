package javacloud.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import javacloud.server.db.SQLHandler;
import javacloud.server.handlers.AuthHandler;
import javacloud.shared.model.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

public class CloudServer {
    private final ServerConfig serverConfig;

    public CloudServer(ServerConfig serverConfig) {
        this.serverConfig = Objects.requireNonNull(serverConfig);
    }

    public void run() {
        SQLHandler.connect();

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
                            new AuthHandler(CloudServer.this)
                        );
                    }
                });

            Channel channel = bootstrap.bind(serverConfig.getPort()).sync().channel();

            System.out.println("Server started on " + channel.localAddress());

            channel.closeFuture().sync();
        } catch (Exception e) {
            System.out.println("Server was broken");
        } finally {
            worker.shutdownGracefully();
            SQLHandler.disconnect();
        }
    }

    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    public String getToken(String username, String password) {
        User user = SQLHandler.getUser(username, password);

        if (user != null) {
            String token = UUID.randomUUID().toString();

            SQLHandler.updateToken(user.getUserName(), token);

            return token;
        }

        return null;
    }

    public String getUserPath(String token) throws IOException {
        User user = SQLHandler.getUser(token);

        if (user == null) {
            return null;
        }

        Path path = Paths.get(serverConfig.getServerDataDirectory(), user.getUserName());

        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }

        return path.toString();
    }
}
