package javacloud.client;

import javacloud.client.events.ClientEventHandler;
import javacloud.client.events.ControllerEventHandler;
import javacloud.shared.model.CloudFile;
import javacloud.shared.model.CloudFilePacket;
import javacloud.shared.request.RequestAuth;
import javacloud.shared.request.RequestGetFile;
import javacloud.shared.request.RequestLs;
import javacloud.shared.request.RequestPutFile;
import javacloud.shared.response.ResponseLs;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ClientController implements Initializable {
    public ListView serverList;
    public ListView clientList;
    public TextArea logText;
    public Label clientPathLabel;
    public Label serverPathLabel;

    private CloudClient cloudClient;
    private SimpleStringProperty clientPath = new SimpleStringProperty("./");
    private SimpleStringProperty serverPath = new SimpleStringProperty("./");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            ClientEventHandler controllerEventHandler = new ControllerEventHandler(this, ClientConfig.get());

            cloudClient = new CloudClient(ClientConfig.get(), controllerEventHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }

        serverPathLabel.textProperty().bind(serverPath);
        clientPathLabel.textProperty().bind(clientPath);

        updateClientFiles();
    }

    //region Actions
    public void actionConnect() {
        if (cloudClient.isConnected()) {
            return;
        }

        new Thread(() -> cloudClient.connect()).start();
    }

    public void actionDisconnect() {
        if (cloudClient.isConnected()) {
            cloudClient.getChannel().close();
        }
    }

    public void actionAuthenticate() {
        if (!cloudClient.isConnected()) {
            return;
        }

        ClientConfig clientConfig = cloudClient.getConfig();
        RequestAuth request = new RequestAuth(clientConfig.getUsername(), clientConfig.getPassword());
        cloudClient.getChannel().writeAndFlush(request);
    }

    public void actionUpdateServerFiles() {
        if (!cloudClient.isAuthenticated()) {
            return;
        }

        cloudClient.getChannel().writeAndFlush(new RequestLs(serverPath.get()));
    }

    public void actionGetFile() {
        if (!cloudClient.isAuthenticated()) {
            return;
        }

        CloudFile cloudFile = (CloudFile) serverList.getSelectionModel().getSelectedItem();

        if (cloudFile == null || cloudFile.isDirectory()) {
            return;
        }

        cloudClient.getChannel().writeAndFlush(new RequestGetFile(Paths.get(serverPath.get(), cloudFile.getRelativePath()).toString()));
    }

    public void actionPutFile() {
        if (!cloudClient.isAuthenticated()) {
            return;
        }

        CloudFile cloudFile = (CloudFile) clientList.getSelectionModel().getSelectedItem();

        if (cloudFile == null || cloudFile.isDirectory()) {
            return;
        }

        ClientConfig clientConfig = cloudClient.getConfig();
        String relativeFilePath = Paths.get(clientPath.get(), cloudFile.getRelativePath()).toString();

        File file = Paths.get(clientConfig.getClientDirectory(), relativeFilePath).toFile();

        long fileSize = file.length();
        int filePacketSize = cloudClient.getConfig().getFilePacketSize();
        long filePacketCount = (fileSize - 1) / filePacketSize + 1;

        for (int i = 0; i < filePacketCount; i++) {
            CloudFilePacket filePacket = new CloudFilePacket(relativeFilePath, fileSize, i, filePacketSize);
            filePacket.readPacket(clientConfig.getClientDirectory());
            cloudClient.getChannel().writeAndFlush(new RequestPutFile(filePacket));
            addLogText(String.format("Send file packet '%s' %d/%d", relativeFilePath, i + 1, filePacketCount));
        }
    }
    //endregion

    //region Control actions
    public void getButtonClick(ActionEvent actionEvent) {
        actionGetFile();
    }

    public void putButtonClick(ActionEvent actionEvent) {
        actionPutFile();
    }

    public void menuConnect(ActionEvent actionEvent) {
        actionConnect();
    }

    public void menuAuthenticate(ActionEvent actionEvent) {
        actionAuthenticate();
    }

    public void menuDisconnect(ActionEvent actionEvent) {
        actionDisconnect();
    }

    public void menuQuit(ActionEvent actionEvent) {
        ((Stage) logText.getScene().getWindow()).close();
    }
    //endregion

    public void addLogText(String text) {
        logText.appendText(text + System.lineSeparator());
    }

    public CloudClient getCloudClient() {
        return cloudClient;
    }

    public void updateClientFiles() {
        clientList.setItems(FXCollections.observableList(getClientFiles()));
    }

    public void updateServerFiles(ResponseLs response) {
        List<CloudFile> list = response.getFiles().stream().map(f -> {
            Path relativePath = Paths.get(serverPath.get()).relativize(Paths.get(f.getRelativePath()));
            return new CloudFile(relativePath.toString(), f.getFileSize(), f.isDirectory());
        }).collect(Collectors.toList());

        list.sort(getCloudFileComparator());

        serverList.setItems(FXCollections.observableList(list));
    }

    private List<CloudFile> getClientFiles() {
        List<CloudFile> fileList = new ArrayList<>();

        File dir = Paths.get(cloudClient.getConfig().getClientDirectory(), clientPath.get()).toFile();

        File[] files = dir.listFiles();

        if (files != null) {
            for (File file : files) {
                fileList.add(new CloudFile(dir.toPath().relativize(file.toPath()).toString(), file.length(), file.isDirectory()));
            }
        }

        fileList.sort(getCloudFileComparator());

        return fileList;
    }

    private Comparator<CloudFile> getCloudFileComparator() {
        return Comparator.comparing((CloudFile f) -> f.isDirectory() ? 0 : 1).thenComparing(f -> f.getRelativePath());
    }
}
