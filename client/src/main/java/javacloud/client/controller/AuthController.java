package javacloud.client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.net.URL;
import java.util.ResourceBundle;

public class AuthController implements Initializable, DialogResult<Pair<String, String>> {
    private Pair<String, String> loginPass;

    public TextField loginField;
    public TextField passwordField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void loginButtonClick(ActionEvent actionEvent) {
        loginPass = new Pair<>(loginField.getText(), passwordField.getText());

        closeStage();
    }

    public void cancelButtonClick(ActionEvent actionEvent) {
        closeStage();
    }

    public void closeStage() {
        ((Stage) (loginField.getScene().getWindow())).close();
    }

    @Override
    public Pair<String, String> CloseResult() {
        return loginPass;
    }
}
