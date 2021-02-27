package javacloud.shared.request;

import javacloud.shared.model.CloudFilePacket;
import javacloud.shared.model.Command;

public class RequestPutFile extends Request {
    private final CloudFilePacket filePacket;

    public RequestPutFile(CloudFilePacket filePacket) {
        super(Command.PUT_FILE);

        this.filePacket = filePacket;
    }

    public CloudFilePacket getFilePacket() {
        return filePacket;
    }
}
