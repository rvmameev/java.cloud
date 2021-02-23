package javacloud.client.tests;

import io.netty.channel.Channel;
import javacloud.client.ClientConfig;
import javacloud.client.CloudClient;
import javacloud.client.events.RunEvents;
import javacloud.shared.request.RequestAuth;

public class Test {
    public static final String HOST = "localhost";
    public static final int PORT = 1180;

    public static void main(String[] args) {
        ClientConfig config = ClientConfig.get();

        CloudClient client = new CloudClient(config);

        client.run(new RunEvents() {
            @Override
            public void afterConnect(Channel channel) {
                channel.writeAndFlush(new RequestAuth(config.getUsername(), config.getPassword()));
            }
        });
    }
}
