package javacloud.server;

import java.io.InputStream;
import java.util.Properties;

public class ServerConfig {

    private int port;
    private String serverDataDirectory;
    private int filePacketSize;

    private String dbDriver;
    private String dbConnectionUrl;

    private ServerConfig() {
    }

    private static final ServerConfig instance;


    static {
        instance = new ServerConfig();

        Properties properties = new Properties();
        try (InputStream inputStream = instance.getClass().getResourceAsStream("app.properties")) {
            properties.load(inputStream);

            instance.port = Integer.parseInt(properties.getProperty("server.port"));
            instance.serverDataDirectory = properties.getProperty("server.dir");
            instance.filePacketSize = Integer.parseInt(properties.getProperty("server.file-packet-size"));

            instance.dbDriver = properties.getProperty("db.driver");
            instance.dbConnectionUrl = properties.getProperty("db.connection-url");
        } catch (Exception e) {
            throw new RuntimeException("Config load error", e);
        }

        // TODO load config from properties file
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

    public String getDbDriver() {
        return dbDriver;
    }

    public String getDbConnectionUrl() {
        return dbConnectionUrl;
    }
}
