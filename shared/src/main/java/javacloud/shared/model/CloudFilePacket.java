package javacloud.shared.model;

import java.io.File;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.Arrays;

public class CloudFilePacket implements Serializable {
    private final String relativeFilePath;
    private final long fileSize;
    private final int packetNumber;
    private final int packetSize;
    private long packetCount;
    private byte[] packet;

    public CloudFilePacket(String relativeFilePath, long fileSize, int packetNumber, int packetSize) {

        this.relativeFilePath = relativeFilePath;
        this.fileSize = fileSize;
        this.packetNumber = packetNumber;
        this.packetSize = packetSize;
        this.packetCount = packetSize == 0 ? 0 : (fileSize - 1) / packetSize + 1;
        this.packet = new byte[packetSize];
    }

    public void readPacket(String basePath) {
        String filePath = Paths.get(basePath, relativeFilePath).toString();

        try (RandomAccessFile file = new RandomAccessFile(filePath, "r")) {
            file.seek(packetNumber * packetSize);
            int len = file.read(packet);
            if (len < packetSize) {
                packet = Arrays.copyOf(packet, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writePacket(String basePath) {
        String filePath = Paths.get(basePath, relativeFilePath).toString();

        File file = new File(filePath);
        File dir = file.getParentFile();

        if (!dir.exists()) {
            dir.mkdirs();
        }

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(filePath, "rw")) {
            randomAccessFile.seek(packetNumber * packetSize);
            randomAccessFile.write(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getRelativeFilePath() {
        return relativeFilePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public int getPacketNumber() {
        return packetNumber;
    }

    public int getPacketSize() {
        return packetSize;
    }

    public long getPacketCount() {
        return packetCount;
    }

    public byte[] getPacket() {
        return packet;
    }
}
