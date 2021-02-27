package javacloud.server.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import javacloud.server.CloudServer;
import javacloud.shared.model.CloudFile;
import javacloud.shared.request.Request;
import javacloud.shared.request.RequestLs;
import javacloud.shared.response.ResponseLs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CommandHandler extends SimpleChannelInboundHandler<Request> {
    private final CloudServer server;

    public CommandHandler(CloudServer server) {
        this.server = server;
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

        context.channel().writeAndFlush(response);
    }
}
