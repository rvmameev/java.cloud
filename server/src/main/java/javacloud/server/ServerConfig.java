package javacloud.server;

public class ServerConfig {

    private int port;
    private String serverDataDirectory;
    private int filePacketSize;

    private String dbDriver;
    private String dbConnectionString;

    private ServerConfig() {
    }

    private static final ServerConfig instance;


    static {
        instance = new ServerConfig();

        // TODO load config from properties file
        instance.port = 1180;
        instance.serverDataDirectory = "./data/server";
        instance.filePacketSize = 10;

        instance.dbDriver = "org.sqlite.JDBC";
        instance.dbConnectionString = "jdbc:sqlite:server/database.db";
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

    public String getDbConnectionString() {
        return dbConnectionString;
    }
}
