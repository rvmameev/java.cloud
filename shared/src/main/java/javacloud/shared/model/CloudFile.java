package javacloud.shared.model;

import java.io.Serializable;
import java.nio.file.Paths;

public class CloudFile implements Serializable {
    private final String relativePath;
    private final long fileSize;
    private final boolean isDirectory;

    public CloudFile(String relativePath, long fileSize, boolean isDirectory) {
        this.relativePath = relativePath;
        this.fileSize = fileSize;
        this.isDirectory = isDirectory;
    }

    public String path(String basePath) {
        return Paths.get(basePath, relativePath).toAbsolutePath().toString();
    }

    public String getRelativePath() {
        return relativePath;
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
            "localPath='" + relativePath + '\'' +
            ", fileSize=" + fileSize +
            ", isDirectory=" + isDirectory +
            '}';
    }
}
