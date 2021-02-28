package javacloud.client.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.serialization.ObjectEncoder;
import javacloud.client.ClientAuthManager;
import javacloud.shared.request.Request;

import java.io.Serializable;

public class ClientAuthHandler extends ObjectEncoder {
    @Override
    protected void encode(ChannelHandlerContext ctx, Serializable msg, ByteBuf out) throws Exception {
        if (msg instanceof Request) {
            ((Request) msg).setToken(ClientAuthManager.getToken());
        }

        super.encode(ctx, msg, out);
    }
}
