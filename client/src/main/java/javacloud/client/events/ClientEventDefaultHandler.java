package javacloud.client.events;

import io.netty.channel.Channel;
import javacloud.client.ClientAuthManager;
import javacloud.client.ClientConfig;
import javacloud.shared.response.ResponseAuth;
import javacloud.shared.response.ResponseGetFile;
import javacloud.shared.response.ResponseLs;
import javacloud.shared.response.ResponsePutFile;

public class ClientEventDefaultHandler implements ClientEventHandler {
    private final ClientConfig config;

    public ClientEventDefaultHandler(ClientConfig config) {
        this.config = config;
    }

    @Override
    public void afterConnect(Channel channel) {

    }

    @Override
    public void afterDisconnect(Channel channel) {
        ClientAuthManager.setToken(null);
    }

    @Override
    public void receiveCommandAuth(Channel channel, ResponseAuth response) {
        ClientAuthManager.setToken(response.getToken());
    }

    @Override
    public void receiveCommandLs(Channel channel, ResponseLs response) {

    }

    @Override
    public void receiveCommandGetFile(Channel channel, ResponseGetFile response) {
        if (response.hasErrors()) {
            return;
        }

        response.getFilePacket().writePacket(config.getClientDirectory());
    }

    @Override
    public void receiveCommandPutFile(Channel channel, ResponsePutFile response) {

    }
}
