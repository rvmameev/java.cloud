package javacloud.client.events;

import io.netty.channel.Channel;

public interface RunEvents {
    void afterConnect(Channel channel);
}
