package javacloud.server;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
         new CloudServer(ServerConfig.get()).run();
    }
}
