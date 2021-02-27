package javacloud.server.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import javacloud.server.CloudServer;
import javacloud.shared.model.Command;
import javacloud.shared.request.Request;
import javacloud.shared.request.RequestAuth;
import javacloud.shared.response.ResponseAuth;
import javacloud.shared.utils.StringUtils;

public class AuthHandler extends SimpleChannelInboundHandler<Request> {
    private final CloudServer server;

    public AuthHandler(CloudServer server) {
        this.server = server;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, Request request) throws Exception {
        String token = null;

        if (request.getCommand() == Command.AUTH) {

            RequestAuth requestAuth = (RequestAuth) request;

            token = server.getToken(requestAuth.getUserName(), requestAuth.getPassword());

            if (!StringUtils.isNullOrEmpty(token)) {
                context.pipeline().remove(AuthHandler.class);
                context.pipeline().addLast(new CommandHandler(server));
                context.pipeline().get(CommandHandler.class).channelActive(context);
            }
        }

        context.writeAndFlush(new ResponseAuth(token));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
