package javacloud.client.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import javacloud.client.CloudClient;
import javacloud.client.events.ClientEventHandler;
import javacloud.shared.response.*;

import java.util.Objects;

public class ClientCommandHandler extends SimpleChannelInboundHandler<Response> {
    private final CloudClient client;

    public ClientCommandHandler(CloudClient client) {
        this.client = Objects.requireNonNull(client);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, Response response) throws Exception {
        ClientEventHandler clientEventHandler = client.getEventHandler();

        switch (response.getCommand()) {
            case AUTH: {
                clientEventHandler.receiveCommandAuth(context.channel(), (ResponseAuth) response);

                break;
            }
            case LS: {
                clientEventHandler.receiveCommandLs(context.channel(), (ResponseLs) response);

                break;
            }
            case GET_FILE: {
                clientEventHandler.receiveCommandGetFile(context.channel(), (ResponseGetFile) response);

                break;
            }
            case PUT_FILE: {
                clientEventHandler.receiveCommandPutFile(context.channel(), (ResponsePutFile) response);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
