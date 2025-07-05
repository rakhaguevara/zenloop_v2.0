package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.UserData;
import util.UserServiceXStream;

public class RegisterController {

    @FXML
    private PasswordField tfConfPass;

    @FXML
    private PasswordField tfpassFieldRegister;

    @FXML
    private TextField tfUsername;

    @FXML
    private TextField tfUserEmail;

    @FXML
    private ChoiceBox<String> cbRoleRegist;

    @FXML
    private Button btnSignUp;

    @FXML
    private Button btnBackLogin;

    @FXML
    private ImageView leftBackLogin;

    @FXML
    public void initialize() {
        // Mengisi pilihan role
        cbRoleRegist.getItems().addAll("Zenloopers", "Admin", "Parent", "Proffessional");
        cbRoleRegist.setValue("Zenloopers");
    }

    @FXML
    private void handleSignUp() {
        String username = tfUsername.getText().trim();
        String email = tfUserEmail.getText().trim();
        String password = tfpassFieldRegister.getText();
        String confirmPassword = tfConfPass.getText();
        String role = cbRoleRegist.getValue();

        // Validasi sederhana
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Form Error", "Please fill in all fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Password Mismatch", "Passwords do not match.");
            return;
        }

        // Buat user baru dan simpan
        UserData newUser = new UserData();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(password); // Untuk keamanan, sebaiknya di-hash
        newUser.setRole(role);

        UserServiceXStream service = new UserServiceXStream();

        // Opsional: Cek apakah username sudah digunakan
        if (service.isUsernameTaken(username)) {
            showAlert(Alert.AlertType.ERROR, "Username Taken", "Username already exists. Please choose another.");
            return;
        }

        service.registerUser(newUser);
        showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "User registered successfully!");

        // Reset form atau pindah ke halaman login
        tfUsername.clear();
        tfUserEmail.clear();
        tfpassFieldRegister.clear();
        tfConfPass.clear();
        cbRoleRegist.setValue("User");
    }

    @FXML
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleBackLogin(ActionEvent event) {
        try {
            Parent backToLogin = FXMLLoader.load(getClass().getResource("/view/login/loginZenloopFXML.fxml"));
            Scene scene = new Scene(backToLogin);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            stage.centerOnScreen();
            stage.setResizable(true);
        } catch (Exception e) {
            System.out.println("Something wrong ");
        }
        ;
    }
}
