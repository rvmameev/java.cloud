package javacloud.client;

public class ClientAuthManager {
    private static String token;

    synchronized public static void setToken(String token) {
        ClientAuthManager.token = token;
    }

    synchronized public static String getToken() {
        return token;
    }
}
