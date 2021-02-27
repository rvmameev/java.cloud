package javacloud.shared.response;

import javacloud.shared.model.Command;
import javacloud.shared.model.CommandMessage;

import java.util.ArrayList;
import java.util.List;

public abstract class Response extends CommandMessage {
    private final List<String> errors;

    public Response(Command command) {
        super(command);
        errors = new ArrayList<>();
    }

    public Response addError(String error) {
        errors.add(error);

        return this;
    }

    public List<String> getErrors() {
        return new ArrayList<>(errors);
    }

    public boolean hasErrors() {
        return errors.size() > 0;
    }


}
