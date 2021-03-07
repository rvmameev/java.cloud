package javacloud.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import javacloud.client.events.ClientEventHandler;
import javacloud.client.handlers.ClientAuthHandler;
import javacloud.client.handlers.ClientCommandHandler;
import javacloud.shared.utils.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class CloudClient {
    private final ClientConfig config;
    private final ClientEventHandler clientEventHandler;
    private Channel channel;
    private Object channelLocker = new Object();

    public CloudClient(ClientConfig config, ClientEventHandler clientEventHandler) throws IOException {
        this.config = Objects.requireNonNull(config);
        this.clientEventHandler = Objects.requireNonNull(clientEventHandler);

        initClientDirectory();
    }

    public void connect() {
        EventLoopGroup worker = new NioEventLoopGroup();

        try {

            synchronized (channelLocker) {
                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(worker)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline().addLast(
                                new ClientAuthHandler(),
                                new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                                new ClientCommandHandler(CloudClient.this)
                            );
                        }
                    });

                channel = bootstrap.connect(config.getServer(), config.getPort()).sync().channel();
            }

            clientEventHandler.afterConnect(channel);

            channel.closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            synchronized (channelLocker) {
                if (channel != null) {
                    clientEventHandler.afterDisconnect(channel);
                }

                channel = null;
            }
            worker.shutdownGracefully();
        }
    }

    public ClientConfig getConfig() {
        return config;
    }

    public ClientEventHandler getEventHandler() {
        return clientEventHandler;
    }

    public Channel getChannel() {
        synchronized (channelLocker) {
            return channel;
        }
    }

    public void disconnect() {
        synchronized (channelLocker) {
            if (channel != null) {
                channel.close();
            }
        }
    }

    public boolean isConnected() {
        return getChannel() != null;
    }

    public boolean isAuthenticated() {
        return !StringUtils.isNullOrEmpty(ClientAuthManager.getToken());
    }

    private void initClientDirectory() throws IOException {
        Path dataPath = Paths.get(config.getClientDirectory());

        if (!Files.exists(dataPath)) {
            Files.createDirectory(dataPath);
        }
    }
}
