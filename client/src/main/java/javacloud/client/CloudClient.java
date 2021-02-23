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
import io.netty.handler.codec.serialization.ObjectEncoder;
import javacloud.client.events.RunEvents;
import javacloud.client.handlers.ClientAuthHandler;
import javacloud.client.handlers.ClientCommandHandler;

public class CloudClient {
    private final ClientConfig config;

    public CloudClient(ClientConfig config) {
        this.config = config;
    }

    public void run() {
        run(null);
    }

    public void run(RunEvents runEvents) {
        EventLoopGroup worker = new NioEventLoopGroup();

        try {

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(worker)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline().addLast(
                            new ObjectEncoder(),
                            new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                            new ClientAuthHandler(),
                            new ClientCommandHandler(CloudClient.this)
                        );
                    }
                });

            Channel channel = bootstrap.connect(config.getServer(), config.getPort()).sync().channel();

            if (runEvents != null) {
                runEvents.afterConnect(channel);
            }

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
}
