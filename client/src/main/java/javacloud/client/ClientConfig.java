package javacloud.client;

import java.io.InputStream;
import java.util.Properties;

public class ClientConfig {
    private String server;
    private int port;
    private String clientDirectory;
    private int filePacketSize;

    private static ClientConfig instance;

    static {
        instance = new ClientConfig();

        Properties properties = new Properties();
        try (InputStream inputStream = instance.getClass().getResourceAsStream("app.properties")) {
            properties.load(inputStream);

            instance.server = properties.getProperty("cloud.server");
            instance.port = Integer.parseInt(properties.getProperty("cloud.port"));
            instance.clientDirectory = properties.getProperty("client.dir");
            instance.filePacketSize = Integer.parseInt(properties.getProperty("client.file-packet-size"));
        } catch (Exception e) {
            throw new RuntimeException("Config load error", e);
        }
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
}
