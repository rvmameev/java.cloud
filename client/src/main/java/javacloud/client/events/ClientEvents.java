package javacloud.client.events;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import javacloud.shared.response.ResponseAuth;
import javacloud.shared.response.ResponseLs;

public interface ClientEvents {
    default void afterConnect(Channel channel) {
    }

    default void onCommandAuth(ChannelHandlerContext context, ResponseAuth response) {
    }

    default void onCommandLs(ChannelHandlerContext context, ResponseLs response) {

    }
}
