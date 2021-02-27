package javacloud.client.tests;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import javacloud.client.ClientConfig;
import javacloud.client.CloudClient;
import javacloud.client.events.ClientEvents;
import javacloud.shared.request.RequestAuth;
import javacloud.shared.request.RequestLs;
import javacloud.shared.response.ResponseAuth;
import javacloud.shared.response.ResponseLs;

public class Test {
    public static void main(String[] args) {
        ClientConfig config = ClientConfig.get();

        ClientEvents clientEvents = new ClientEvents() {
            @Override
            public void afterConnect(Channel channel) {
                channel.writeAndFlush(new RequestAuth(config.getUsername(), config.getPassword()));
            }

            @Override
            public void onCommandAuth(ChannelHandlerContext context, ResponseAuth response) {
                context.writeAndFlush(new RequestLs("./"));
            }

            @Override
            public void onCommandLs(ChannelHandlerContext context, ResponseLs response) {
                System.out.println(response.getFiles());
            }
        };

        CloudClient client = new CloudClient(config, clientEvents);

        client.run();
    }
}
