package javacloud.client.events;

import io.netty.channel.Channel;
import javacloud.client.ClientConfig;
import javacloud.client.ClientController;
import javacloud.shared.model.CloudFilePacket;
import javacloud.shared.response.ResponseAuth;
import javacloud.shared.response.ResponseGetFile;
import javacloud.shared.response.ResponseLs;
import javacloud.shared.response.ResponsePutFile;
import javacloud.shared.utils.StringUtils;

import java.util.Objects;

public class ControllerEventHandler extends ClientEventDefaultHandler {
    private final ClientController controller;

    public ControllerEventHandler(ClientController controller, ClientConfig config) {
        super(config);

        this.controller = Objects.requireNonNull(controller);
    }

    @Override
    public void afterConnect(Channel channel) {
        super.afterConnect(channel);

        controller.addLogText("Connected to " + channel.remoteAddress());

        controller.actionAuthenticate();
    }

    @Override
    public void afterDisconnect(Channel channel) {
        super.afterDisconnect(channel);

        controller.addLogText("Disconnected");
    }

    @Override
    public void receiveCommandAuth(Channel channel, ResponseAuth response) {
        super.receiveCommandAuth(channel, response);

        if (StringUtils.isNullOrEmpty(response.getToken())) {
            controller.addLogText("Authentication error");
        } else {
            controller.addLogText("Authentication successful");

            controller.actionUpdateServerFiles();
        }
    }

    @Override
    public void receiveCommandLs(Channel channel, ResponseLs response) {
        super.receiveCommandLs(channel, response);

        controller.updateServerFiles(response);
    }

    @Override
    public void receiveCommandGetFile(Channel channel, ResponseGetFile response) {
        super.receiveCommandGetFile(channel, response);

        CloudFilePacket filePacket = response.getFilePacket();

        controller.addLogText(String.format("Received file packet '%s' %d/%d", filePacket.getRelativeFilePath(), filePacket.getPacketNumber() + 1, filePacket.getPacketCount()));
    }

    @Override
    public void receiveCommandPutFile(Channel channel, ResponsePutFile response) {
        super.receiveCommandPutFile(channel, response);

        CloudFilePacket filePacket = response.getFilePacket();
        controller.addLogText(String.format("Server received file packet '%s' %d/%d", filePacket.getRelativeFilePath(), filePacket.getPacketNumber() + 1, filePacket.getPacketCount()));
    }
}
