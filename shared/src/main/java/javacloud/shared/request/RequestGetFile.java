package javacloud.shared.request;

import javacloud.shared.model.Command;

public class RequestGetFile extends Request {
    private final String relativeFilePath;

    public RequestGetFile(String relativeFilePath) {
        super(Command.GET_FILE);
        this.relativeFilePath = relativeFilePath;
    }

    public String getRelativeFilePath() {
        return relativeFilePath;
    }
}
