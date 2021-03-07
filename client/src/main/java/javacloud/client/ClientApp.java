package javacloud.client;

import io.netty.channel.Channel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../main.fxml"));
        Parent main = loader.load();
        ClientController controller = loader.getController();
        primaryStage.setScene(new Scene(main));
        primaryStage.setTitle("Cloud");
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(req -> {
            Channel channel = controller.getCloudClient().getChannel();

            if (channel != null) {
                channel.close();
            }
        });
        primaryStage.show();
    }
}
