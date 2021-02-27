package javacloud.shared.response;

import javacloud.shared.model.CloudFilePacket;
import javacloud.shared.model.Command;

public class ResponseGetFile extends Response {
    private final CloudFilePacket filePacket;

    public ResponseGetFile(CloudFilePacket filePacket) {
        super(Command.GET_FILE);
        this.filePacket = filePacket;
    }

    public CloudFilePacket getFilePacket() {
        return filePacket;
    }
}
