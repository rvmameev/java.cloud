package javacloud.client;

import io.netty.channel.Channel;
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
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    public ListView serverListView;
    public ListView clientListView;
    public TextArea logText;
    public Label clientPathLabel;
    public Label serverPathLabel;

    private CloudClient cloudClient;
    private SimpleStringProperty clientPath = new SimpleStringProperty("./");
    private SimpleStringProperty serverPath = new SimpleStringProperty("./");
    private ObservableList<CloudFile> serverList = FXCollections.observableList(new ArrayList<>());
    private ObservableList<CloudFile> clientList = FXCollections.observableList(new ArrayList<>());

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

        serverListView.setItems(serverList);
        clientListView.setItems(clientList);

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

        serverList.clear();
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

        CloudFile cloudFile = (CloudFile) serverListView.getSelectionModel().getSelectedItem();

        if (cloudFile == null || cloudFile.isDirectory()) {
            return;
        }

        cloudClient.getChannel().writeAndFlush(new RequestGetFile(Paths.get(serverPath.get(), cloudFile.getRelativePath()).toString()));
    }

    public void actionPutFile() {
        if (!cloudClient.isAuthenticated()) {
            return;
        }

        CloudFile cloudFile = (CloudFile) clientListView.getSelectionModel().getSelectedItem();

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

    public void actionClose() {
        Channel channel = cloudClient.getChannel();

        if (channel != null) {
            channel.close();
        }

        ((Stage) logText.getScene().getWindow()).close();
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
        actionClose();
    }

    public void serverRefreshButtonClick(ActionEvent actionEvent) {
        actionUpdateServerFiles();
    }

    public void serverUpButtonClick(ActionEvent actionEvent) {
        Path parent = Paths.get(serverPath.get()).getParent();

        if (parent == null) {
            return;
        }

        serverPath.set(parent.toString());

        actionUpdateServerFiles();
    }

    public void serverListMouseClicked(MouseEvent mouseEvent) {
        if (!cloudClient.isAuthenticated()) {
            return;
        }

        CloudFile cloudFile = (CloudFile) serverListView.getSelectionModel().getSelectedItem();

        if (cloudFile == null || !cloudFile.isDirectory()) {
            return;
        }

        serverPath.set(Paths.get(serverPath.get(), cloudFile.getRelativePath()).toString());

        actionUpdateServerFiles();
    }

    public void clientRefreshButtonClick(ActionEvent actionEvent) {
        updateClientFiles();
    }

    public void clientUpButtonClick(ActionEvent actionEvent) {
        Path parent = Paths.get(clientPath.get()).getParent();

        if (parent == null) {
            return;
        }

        clientPath.set(parent.toString());

        updateClientFiles();
    }

    public void clientListMouseClicked(MouseEvent mouseEvent) {
        CloudFile cloudFile = (CloudFile) clientListView.getSelectionModel().getSelectedItem();

        if (cloudFile == null || !cloudFile.isDirectory()) {
            return;
        }

        clientPath.set(Paths.get(clientPath.get(), cloudFile.getRelativePath()).toString());

        updateClientFiles();
    }
    //endregion

    public void addLogText(String text) {
        logText.appendText(text + System.lineSeparator());
    }

    public void updateClientFiles() {
        clientList.clear();

        File dir = Paths.get(cloudClient.getConfig().getClientDirectory(), clientPath.get()).toFile();

        File[] files = dir.listFiles();

        if (files != null) {
            for (File file : files) {
                clientList.add(new CloudFile(dir.toPath().relativize(file.toPath()).toString(), file.length(), file.isDirectory()));
            }
        }

        clientList.sort(getCloudFileComparator());
    }

    public void updateServerFiles(ResponseLs response) {
        serverList.clear();

        response.getFiles().forEach(f -> {
            Path relativePath = Paths.get(serverPath.get()).relativize(Paths.get(f.getRelativePath()));
            serverList.add(new CloudFile(relativePath.toString(), f.getFileSize(), f.isDirectory()));
        });

        serverList.sort(getCloudFileComparator());
    }

    private Comparator<CloudFile> getCloudFileComparator() {
        return Comparator.comparing((CloudFile f) -> f.isDirectory() ? 0 : 1).thenComparing(f -> f.getRelativePath());
    }
}
