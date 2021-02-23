package javacloud.client.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import javacloud.client.ClientAuth;
import javacloud.shared.request.Request;

public class ClientAuthHandler extends MessageToByteEncoder<Request> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Request request, ByteBuf byteBuf) throws Exception {
        request.setToken(ClientAuth.getToken());
    }
}
