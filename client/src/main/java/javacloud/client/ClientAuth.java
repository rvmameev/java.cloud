package javacloud.client;

public class ClientAuth {
    private static String token;

    synchronized public static void setToken(String token) {
        ClientAuth.token = token;
    }

    synchronized public static String getToken() {
        return token;
    }
}
