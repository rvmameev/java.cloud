package javacloud.shared.response;

import javacloud.shared.model.CloudFilePacket;
import javacloud.shared.model.Command;

import java.util.Objects;

public class ResponsePutFile extends Response {
    private final CloudFilePacket filePacket;

    public ResponsePutFile(CloudFilePacket filePacket) {
        super(Command.PUT_FILE);
        this.filePacket = Objects.requireNonNull(filePacket);
    }

    public CloudFilePacket getFilePacket() {
        return filePacket;
    }
}
