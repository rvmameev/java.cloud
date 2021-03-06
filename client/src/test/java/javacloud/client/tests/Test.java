package javacloud.client.tests;

import javacloud.client.ClientConfig;
import javacloud.client.CloudClient;
import javacloud.client.events.ClientEventDefaultHandler;
import javacloud.client.events.ClientEventHandler;

import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
        ClientConfig config = ClientConfig.get();

        ClientEventHandler clientEventHandler = new ClientEventDefaultHandler(config);

        ClientEventHandler testClientEventHandler = new TestClientEventHandler(config, clientEventHandler);

        CloudClient client = new CloudClient(config, testClientEventHandler);

        client.connect();
    }
}
