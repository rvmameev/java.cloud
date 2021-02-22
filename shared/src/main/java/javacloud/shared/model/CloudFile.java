package javacloud.shared.model;

import java.io.Serializable;
import java.nio.file.Paths;

public class CloudFile implements Serializable {
    private String localPath;
    private long fileSize;
    private boolean isDirectory;

    public CloudFile(String localPath, long fileSize, boolean isDirectory) {
        this.localPath = localPath;
        this.fileSize = fileSize;
        this.isDirectory = isDirectory;
    }

    public String path(String basePath) {
        return Paths.get(basePath, localPath).toAbsolutePath().toString();
    }

    public String getLocalPath() {
        return localPath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    @Override
    public String toString() {
        return "CloudFile{" +
            "localPath='" + localPath + '\'' +
            ", fileSize=" + fileSize +
            ", isDirectory=" + isDirectory +
            '}';
    }
}
