package javacloud.client.events;

import io.netty.channel.Channel;
import javacloud.shared.response.ResponseAuth;
import javacloud.shared.response.ResponseGetFile;
import javacloud.shared.response.ResponseLs;
import javacloud.shared.response.ResponsePutFile;

public interface ClientEventHandler {
    void afterConnect(Channel channel);

    void afterDisconnect(Channel channel);

    void receiveCommandAuth(Channel channel, ResponseAuth response) throws Exception;

    void receiveCommandLs(Channel channel, ResponseLs response);

    void receiveCommandGetFile(Channel channel, ResponseGetFile response);

    void receiveCommandPutFile(Channel channel, ResponsePutFile response);
}
