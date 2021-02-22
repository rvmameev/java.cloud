package javacloud.shared.request;

import javacloud.shared.model.Command;
import javacloud.shared.model.CommandMessage;

public class Request extends CommandMessage {
    public Request(Command command) {
        super(command);
    }
}
