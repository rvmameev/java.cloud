package javacloud.client;

import javacloud.client.controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent main = loader.load();
        MainController controller = loader.getController();
        primaryStage.setScene(new Scene(main));
        primaryStage.setTitle("Cloud");
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(req -> {
            controller.stageClose();
        });
        primaryStage.show();
    }
}
