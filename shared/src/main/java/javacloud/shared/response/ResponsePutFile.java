package javacloud.shared.response;

import javacloud.shared.model.Command;

public class ResponsePutFile extends Response {
    public ResponsePutFile() {
        super(Command.PUT_FILE);
    }
}
