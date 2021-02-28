package javacloud.client.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import javacloud.client.CloudClient;
import javacloud.client.events.ClientEvents;
import javacloud.shared.response.*;

import java.util.Objects;

public class ClientCommandHandler extends SimpleChannelInboundHandler<Response> {
    private final CloudClient client;

    public ClientCommandHandler(CloudClient client) {
        this.client = Objects.requireNonNull(client);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, Response response) throws Exception {
        ClientEvents clientEvents = client.getClientEvents();

        switch (response.getCommand()) {
            case AUTH: {
                clientEvents.receiveCommandAuth(context.channel(), (ResponseAuth) response);

                break;
            }
            case LS: {
                clientEvents.receiveCommandLs(context.channel(), (ResponseLs) response);

                break;
            }
            case GET_FILE: {
                clientEvents.receiveCommandGetFile(context.channel(), (ResponseGetFile) response);

                break;
            }
            case PUT_FILE: {
                clientEvents.receiveCommandPutFile(context.channel(), (ResponsePutFile) response);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
