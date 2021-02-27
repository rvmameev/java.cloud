package javacloud.shared.response;

import javacloud.shared.model.CloudFile;
import javacloud.shared.model.Command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResponseLs extends Response {
    private final List<CloudFile> files = new ArrayList<>();

    public ResponseLs() {
        super(Command.LS);
    }

    public void addFile(CloudFile file) {
        files.add(file);
    }

    public Iterable<CloudFile> getFiles() {
        return Collections.unmodifiableList(files);
    }
}
