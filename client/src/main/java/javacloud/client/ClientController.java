package javacloud.client;

import javacloud.client.events.ClientEventHandler;
import javacloud.client.events.ClientEventHandlerImpl;
import javacloud.client.events.ControllerEventHandler;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    public ListView serverList;
    public ListView clientList;
    public TextArea logText;

    private CloudClient cloudClient;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            ClientEventHandler implHandler = new ClientEventHandlerImpl(ClientConfig.get());

            ClientEventHandler controllerEventHandler = new ControllerEventHandler(this, implHandler);

            cloudClient = new CloudClient(ClientConfig.get(), controllerEventHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getButtonClick(ActionEvent actionEvent) {
    }

    public void putButtonClick(ActionEvent actionEvent) {
    }

    public void menuConnect(ActionEvent actionEvent) {
        if (cloudClient.isConnected()) {
            return;
        }

        new Thread(() -> cloudClient.connect()).start();
    }

    public void menuDisconnect(ActionEvent actionEvent) {
        if (cloudClient.isConnected()) {
            cloudClient.getChannel().close();
        }
    }

    public void addLogText(String text) {
        logText.appendText(text + System.lineSeparator());
    }

    public CloudClient getCloudClient() {
        return cloudClient;
    }
}
