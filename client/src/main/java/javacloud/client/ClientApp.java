package javacloud.client;

import javacloud.client.events.ClientEvents;

public class ClientApp {
    public static void main(String[] args) {
        ClientEvents clientEvents = new ClientEvents() {
        };

        new CloudClient(ClientConfig.get(), clientEvents).run();
    }
}
