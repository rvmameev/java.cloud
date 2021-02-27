package javacloud.shared.model;

import java.io.RandomAccessFile;
import java.nio.file.Paths;
import java.util.Arrays;

public class CloudFilePacket {
    private final String relativePath;
    private final int fileSize;
    private final int packetNumber;
    private final int packetSize;
    private byte[] packet;

    public CloudFilePacket(String relativePath, int fileSize, int packetNumber, int packetSize) {

        this.relativePath = relativePath;
        this.fileSize = fileSize;
        this.packetNumber = packetNumber;
        this.packetSize = packetSize;
        this.packet = new byte[packetSize];
    }

    public void readPacket(String basePath) {
        String filePath = Paths.get(basePath, relativePath).toString();

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
        String filePath = Paths.get(basePath, relativePath).toString();

        try (RandomAccessFile file = new RandomAccessFile(filePath, "rw")) {
            file.seek(packetNumber * packetSize);
            file.write(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getRelativePath() {
        return relativePath;
    }

    public int getFileSize() {
        return fileSize;
    }

    public int getPacketNumber() {
        return packetNumber;
    }

    public int getPacketSize() {
        return packetSize;
    }

    public byte[] getPacket() {
        return packet;
    }
}
