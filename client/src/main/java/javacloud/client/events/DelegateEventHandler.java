package javacloud.client.events;

import io.netty.channel.Channel;
import javacloud.shared.response.ResponseAuth;
import javacloud.shared.response.ResponseGetFile;
import javacloud.shared.response.ResponseLs;
import javacloud.shared.response.ResponsePutFile;

import java.util.Objects;

public abstract class DelegateEventHandler implements ClientEventHandler {
    private final ClientEventHandler innerHandler;

    public DelegateEventHandler(ClientEventHandler innerHandler) {
        this.innerHandler = Objects.requireNonNull(innerHandler);
    }

    @Override
    public void afterConnect(Channel channel) {
        innerHandler.afterConnect(channel);
    }

    @Override
    public void afterDisconnect(Channel channel) {
        innerHandler.afterDisconnect(channel);
    }

    @Override
    public void receiveCommandAuth(Channel channel, ResponseAuth response) throws Exception {
        innerHandler.receiveCommandAuth(channel, response);
    }

    @Override
    public void receiveCommandLs(Channel channel, ResponseLs response) {
        innerHandler.receiveCommandLs(channel, response);
    }

    @Override
    public void receiveCommandGetFile(Channel channel, ResponseGetFile response) {
        innerHandler.receiveCommandGetFile(channel, response);
    }

    @Override
    public void receiveCommandPutFile(Channel channel, ResponsePutFile response) {
        innerHandler.receiveCommandPutFile(channel, response);
    }
}
