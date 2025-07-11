package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;

public class SettingController {

    @FXML
    private TextField tfEmail, tfName, tfPassword, tfPhoneNum, tfUsername;

    @FXML
    private Circle ccProfileImg;

    @FXML
    private Button btnSaveChanges, btnLogOutSet;

    // Handler untuk menyimpan perubahan
    @FXML
    void handleSaveChanges(ActionEvent event) {

    }

    // Handler untuk logout
    @FXML
    void handleLogOut(ActionEvent event) {

    }

    // Tambahkan handler lainnya hanya jika benar-benar dibutuhkan
}
