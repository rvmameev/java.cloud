package javacloud.client;

public class ClientApp {
    public static void main(String[] args) {
        new CloudClient(ClientConfig.get()).run();
    }
}
