package javacloud.shared.request;

import javacloud.shared.model.Command;
import javacloud.shared.model.CommandMessage;

public class Request extends CommandMessage {
    private String token = null;

    public Request(Command command) {
        super(command);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
