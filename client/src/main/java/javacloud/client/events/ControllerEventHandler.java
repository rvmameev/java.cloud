package javacloud.client.events;

import io.netty.channel.Channel;
import javacloud.client.ClientController;
import javacloud.shared.response.ResponseAuth;
import javacloud.shared.response.ResponseGetFile;
import javacloud.shared.response.ResponseLs;
import javacloud.shared.response.ResponsePutFile;

import java.util.Objects;

public class ControllerEventHandler extends DelegateEventHandler{
    private final ClientController controller;

    public ControllerEventHandler(ClientController controller, ClientEventHandler innerHandler) {
        super(innerHandler);
        this.controller = Objects.requireNonNull(controller);
    }

    @Override
    public void afterConnect(Channel channel) {
        super.afterConnect(channel);

        controller.addLogText("connected to " + channel.remoteAddress());
    }

    @Override
    public void afterDisconnect(Channel channel) {
        super.afterDisconnect(channel);

        controller.addLogText("disconnected");
    }

    @Override
    public void receiveCommandAuth(Channel channel, ResponseAuth response) throws Exception {
        super.receiveCommandAuth(channel, response);
    }

    @Override
    public void receiveCommandLs(Channel channel, ResponseLs response) {
        super.receiveCommandLs(channel, response);
    }

    @Override
    public void receiveCommandGetFile(Channel channel, ResponseGetFile response) {
        super.receiveCommandGetFile(channel, response);
    }

    @Override
    public void receiveCommandPutFile(Channel channel, ResponsePutFile response) {
        super.receiveCommandPutFile(channel, response);
    }
}
