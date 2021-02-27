package javacloud.client.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import javacloud.client.ClientAuth;
import javacloud.client.CloudClient;
import javacloud.shared.response.Response;
import javacloud.shared.response.ResponseAuth;
import javacloud.shared.response.ResponseLs;
import javacloud.shared.utils.StringUtils;

import java.util.Objects;

public class ClientCommandHandler extends SimpleChannelInboundHandler<Response> {
    private final CloudClient client;

    public ClientCommandHandler(CloudClient client) {
        this.client = Objects.requireNonNull(client);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, Response response) throws Exception {
        switch (response.getCommand()) {
            case AUTH: {
                ResponseAuth responseAuth = (ResponseAuth) response;

                if (StringUtils.isNullOrEmpty(responseAuth.getToken())) {
                    throw new Exception("Auth error");
                }

                ClientAuth.setToken(responseAuth.getToken());

                client.getClientEvents().onCommandAuth(context, responseAuth);

                break;
            }
            case LS: {
                client.getClientEvents().onCommandLs(context, (ResponseLs) response);

                break;
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
