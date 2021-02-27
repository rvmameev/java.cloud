package javacloud.shared.response;

import javacloud.shared.model.Command;

public class ResponseAuth extends Response {

    private final String token;

    public ResponseAuth(String token) {
        super(Command.AUTH);
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
