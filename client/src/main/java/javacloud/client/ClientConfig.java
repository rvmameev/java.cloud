package javacloud.client;

public class ClientConfig {
    private String server;
    private int port;
    private String clientDirectory;

    private String username;
    private String password;

    private static ClientConfig instance;

    static {
        instance = new ClientConfig();

        // load config
        instance.server = "localhost";
        instance.port = 1180;
        instance.clientDirectory = "./client";

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

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
