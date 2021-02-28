package javacloud.server;

public class ServerConfig {

    private int port;
    private String serverDataDirectory;
    private int filePacketSize;

    private ServerConfig() {
    }

    private static final ServerConfig instance;

    static {
        instance = new ServerConfig();

        // load config
        instance.port = 1180;
        instance.serverDataDirectory = "./data/server";
        instance.filePacketSize = 10;
    }

    public static ServerConfig get() {
        return instance;
    }

    public int getPort() {
        return port;
    }

    public String getServerDataDirectory() {
        return serverDataDirectory;
    }

    public int getFilePacketSize() {
        return filePacketSize;
    }
}
