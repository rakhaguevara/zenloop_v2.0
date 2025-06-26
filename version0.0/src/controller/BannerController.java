package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Node;

public class BannerController {

    @FXML
    private Button btnFirstLogin;

    @FXML
    private Button btnFirstSignUp;

    @FXML
    public void handleButtonOpenLogin(ActionEvent event) {

        try {
            Parent secondPage = FXMLLoader.load(getClass().getResource("/view/login/loginCalmifyFXML.fxml"));
            Scene scene = new Scene(secondPage);

            // Ganti scene pada stage yang aktif
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            stage.centerOnScreen();

            System.out.println("Halaman Login berhasil tampil");

        } catch (Exception e) {
            System.err.println("Kesalahan tidak terduga (File tidak terbaca, fxml format salah) ");
        }
    }

    @FXML
    public void handleButtonOpenSignUp(ActionEvent event) {
        try {
            Parent secondPage = FXMLLoader.load(getClass().getResource("/view/Register/registerFXML.fxml"));
            Scene scene = new Scene(secondPage);

            // Ganti scene pada stage yang aktif
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            stage.centerOnScreen();

            stage.fullScreenProperty();

            System.out.println("Halaman Login berhasil tampil");

        } catch (Exception e) {
            System.err.println("Kesalahan tidak terduga (File tidak terbaca, fxml format salah) ");
        }
    }

}
