package javacloud.client;

public class ClientConfig {
    private String server;
    private int port;
    private String clientDirectory;
    private int filePacketSize;

    private String username;
    private String password;

    private static ClientConfig instance;

    static {
        instance = new ClientConfig();

        // TODO load config from properties file
        instance.server = "localhost";
        instance.port = 1180;
        instance.clientDirectory = "./data/client";
        instance.filePacketSize = 11;

        instance.username = "user1";
        instance.password = "pass1";
    }

    private ClientConfig() {
    }

    public static ClientConfig get() {
        return instance;
    }

    public String getServer() {
        return server;
    }

    public int getPort() {
        return port;
    }

    public String getClientDirectory() {
        return clientDirectory;
    }

    public int getFilePacketSize() {
        return filePacketSize;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
