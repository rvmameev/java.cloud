package javacloud.shared.request;

import javacloud.shared.model.Command;

public class RequestLs extends Request {
    private final String path;

    public RequestLs(String path) {
        super(Command.LS);
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
