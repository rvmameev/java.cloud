package javacloud.client.tests;

import io.netty.channel.Channel;
import javacloud.client.ClientConfig;
import javacloud.client.events.ClientEventHandler;
import javacloud.shared.model.CloudFilePacket;
import javacloud.shared.request.RequestAuth;
import javacloud.shared.request.RequestGetFile;
import javacloud.shared.request.RequestLs;
import javacloud.shared.request.RequestPutFile;
import javacloud.shared.response.ResponseAuth;
import javacloud.shared.response.ResponseGetFile;
import javacloud.shared.response.ResponseLs;
import javacloud.shared.response.ResponsePutFile;

import java.io.File;
import java.nio.file.Paths;
import java.util.stream.StreamSupport;

public class TestClientEventHandler implements ClientEventHandler {
    private final ClientConfig clientConfig;
    private final ClientEventHandler innerEvents;

    public TestClientEventHandler(ClientConfig clientConfig, ClientEventHandler innerEvents) {
        this.innerEvents = innerEvents;
        this.clientConfig = clientConfig;
    }

    @Override
    public void afterConnect(Channel channel) {
        innerEvents.afterConnect(channel);

        System.out.println("Connected to server " + channel.remoteAddress());

        System.out.println("Auth to server");
        channel.writeAndFlush(new RequestAuth(clientConfig.getUsername(), clientConfig.getPassword()));
    }

    @Override
    public void afterDisconnect(Channel channel) {

    }

    @Override
    public void receiveCommandAuth(Channel channel, ResponseAuth response) {
        innerEvents.receiveCommandAuth(channel, response);

        System.out.println(String.format("Received token from server (%s)", response.getToken()));

        System.out.println("Get file list");
        channel.writeAndFlush(new RequestLs("./"));
    }

    @Override
    public void receiveCommandLs(Channel channel, ResponseLs response) {
        innerEvents.receiveCommandLs(channel, response);

        StreamSupport.stream(response.getFiles().spliterator(), false).forEach(f -> {
            System.out.println(String.format("%s\t%s\t%d", f.isDirectory() ? "DIR" : "FILE", f.getRelativePath(), f.getFileSize()));
        });

        String relativeFilePath = "./1.txt";
        System.out.println(String.format("Get file '%s'", relativeFilePath));
        channel.writeAndFlush(new RequestGetFile(relativeFilePath));

        relativeFilePath = "./2.txt";
        System.out.println(String.format("Get file '%s'", relativeFilePath));
        channel.writeAndFlush(new RequestGetFile(relativeFilePath));

        relativeFilePath = "./dir/client.data.txt";
        System.out.println(String.format("Put file '%s'", relativeFilePath));

        File file = Paths.get(clientConfig.getClientDirectory(), relativeFilePath).toFile();

        long fileSize = file.length();
        int filePacketSize = clientConfig.getFilePacketSize();
        long filePacketCount = (fileSize - 1) / filePacketSize + 1;

        for (int i = 0; i < filePacketCount; i++) {
            CloudFilePacket filePacket = new CloudFilePacket(relativeFilePath, fileSize, i, filePacketSize);
            filePacket.readPacket(clientConfig.getClientDirectory());
            channel.writeAndFlush(new RequestPutFile(filePacket));
            System.out.println(String.format("Send file packet '%s' %d/%d", relativeFilePath, i + 1, filePacketCount));
        }
    }

    @Override
    public void receiveCommandGetFile(Channel channel, ResponseGetFile response) {
        innerEvents.receiveCommandGetFile(channel, response);

        if (response.hasErrors()) {
            response.getErrors().stream().forEach(e -> System.out.println(e));
            return;
        }

        CloudFilePacket filePacket = response.getFilePacket();
        System.out.println(String.format("Receive file packet '%s' %d/%d", filePacket.getRelativeFilePath(), filePacket.getPacketNumber() + 1, filePacket.getPacketCount()));
    }

    @Override
    public void receiveCommandPutFile(Channel channel, ResponsePutFile response) {
        innerEvents.receiveCommandPutFile(channel, response);

        CloudFilePacket filePacket = response.getFilePacket();
        System.out.println(String.format("Server received file packet '%s' %d/%d", filePacket.getRelativeFilePath(), filePacket.getPacketNumber() + 1, filePacket.getPacketCount()));
    }
}
