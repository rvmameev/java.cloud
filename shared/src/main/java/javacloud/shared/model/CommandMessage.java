package javacloud.shared.model;

import java.io.Serializable;

public abstract class CommandMessage implements Serializable {
    private final Command command;

    public CommandMessage(Command command) {
        this.command = command;
    }

    public Command getCommand() {
        return command;
    }
}
