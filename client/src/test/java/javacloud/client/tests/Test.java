package javacloud.client.tests;

import javacloud.client.ClientConfig;
import javacloud.client.CloudClient;
import javacloud.client.events.ClientEvents;
import javacloud.client.events.ClientEventsImpl;

import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
        ClientConfig config = ClientConfig.get();

        ClientEvents clientEvents = new ClientEventsImpl(config);

        ClientEvents testClientEvents = new TestClientEvents(config, clientEvents);

        CloudClient client = new CloudClient(config, testClientEvents);

        client.run();
    }
}
