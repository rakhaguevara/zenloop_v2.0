package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class GoogleLoginController {

    @FXML
    private Button btnBackLoginToPage;

    @FXML
    private Button btnRegisterPage;

    @FXML
    private void handleRegister(ActionEvent event) {
        try {
            Parent registerPage = FXMLLoader.load(getClass().getResource("/view/Register/registerFXML.fxml"));
            Scene scene = new Scene(registerPage);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            stage.centerOnScreen();
        } catch (Exception e) {
            System.out.println("Error loading register page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        try {
            Parent loginPage = FXMLLoader.load(getClass().getResource("/view/login/loginZenloopFXML.fxml"));
            Scene scene = new Scene(loginPage);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            stage.centerOnScreen();
        } catch (Exception e) {
            System.out.println("Kesalahan dalam memuat halaman");
        }
    }

}
