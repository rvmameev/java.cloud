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
import javacloud.client.events.ClientEvents;
import javacloud.client.handlers.ClientAuthHandler;
import javacloud.client.handlers.ClientCommandHandler;

import java.util.Objects;

public class CloudClient {
    private final ClientConfig config;
    private final ClientEvents clientEvents;

    public CloudClient(ClientConfig config, ClientEvents clientEvents) {
        this.config = Objects.requireNonNull(config);
        this.clientEvents = Objects.requireNonNull(clientEvents);
    }

    public void run() {
        EventLoopGroup worker = new NioEventLoopGroup();

        try {

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

            Channel channel = bootstrap.connect(config.getServer(), config.getPort()).sync().channel();

            clientEvents.afterConnect(channel);

            channel.closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            worker.shutdownGracefully();
        }
    }

    public ClientConfig getConfig() {
        return config;
    }

    public ClientEvents getClientEvents() {
        return clientEvents;
    }
}
