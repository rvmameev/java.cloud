package javacloud.server.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import javacloud.server.CloudServer;
import javacloud.shared.model.CloudFile;
import javacloud.shared.model.CloudFilePacket;
import javacloud.shared.request.Request;
import javacloud.shared.request.RequestGetFile;
import javacloud.shared.request.RequestLs;
import javacloud.shared.request.RequestPutFile;
import javacloud.shared.response.ResponseGetFile;
import javacloud.shared.response.ResponseLs;
import javacloud.shared.response.ResponsePutFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class CommandHandler extends SimpleChannelInboundHandler<Request> {
    private final CloudServer server;

    public CommandHandler(CloudServer server) {
        this.server = Objects.requireNonNull(server);
    }

    @Override
    public void channelActive(ChannelHandlerContext context) throws Exception {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, Request request) throws Exception {
        switch (request.getCommand()) {
            case LS: {
                sendResponseLs(context, (RequestLs) request);
                break;
            }
            case GET_FILE: {
                sendResponseGetFile(context, (RequestGetFile) request);
                break;
            }
            case PUT_FILE: {
                sendResponsePutFile(context, (RequestPutFile) request);
                break;
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    private void sendResponseLs(ChannelHandlerContext context, RequestLs request) throws IOException {
        Path userPath = Paths.get(server.getUserPath(request.getToken()));

        File userDir = Paths.get(userPath.toString(), request.getPath()).toFile();

        ResponseLs response = new ResponseLs();

        File[] files = userDir.listFiles();

        if (files != null) {
            for (File file : files) {
                response.addFile(new CloudFile(userPath.relativize(Paths.get(file.getPath())).toString(), file.length(), file.isDirectory()));
            }
        }

        context.writeAndFlush(response);
    }

    private void sendResponseGetFile(ChannelHandlerContext context, RequestGetFile request) throws IOException {
        String userPath = server.getUserPath(request.getToken());
        File file = Paths.get(userPath, request.getRelativeFilePath()).toFile();

        if (!file.exists()) {
            CloudFilePacket filePacket = new CloudFilePacket(request.getRelativeFilePath(), 0, 0, 0);
            ResponseGetFile response = new ResponseGetFile(filePacket);
            response.addError(String.format("File not found (%s)", request.getRelativeFilePath()));

            context.writeAndFlush(response);

            return;
        }

        long fileSize = file.length();
        int filePacketSize = server.getServerConfig().getFilePacketSize();
        long filePacketCount = (fileSize - 1) / filePacketSize + 1;

        for (int i = 0; i < filePacketCount; i++) {
            CloudFilePacket filePacket = new CloudFilePacket(request.getRelativeFilePath(), fileSize, i, filePacketSize);
            filePacket.readPacket(userPath);

            context.writeAndFlush(new ResponseGetFile(filePacket));
        }
    }

    private void sendResponsePutFile(ChannelHandlerContext context, RequestPutFile request) throws IOException {
        String userPath = server.getUserPath(request.getToken());
        CloudFilePacket packet = request.getFilePacket();
        packet.writePacket(userPath);

        CloudFilePacket emptyPacket = new CloudFilePacket(packet.getRelativeFilePath(), packet.getFileSize(), packet.getPacketNumber(), packet.getPacketSize());

        context.writeAndFlush(new ResponsePutFile(emptyPacket));
    }
}
