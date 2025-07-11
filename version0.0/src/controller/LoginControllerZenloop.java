package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.UserData;
import util.UserServiceXStream;
import javafx.scene.Node;

public class LoginControllerZenloop {

    @FXML
    private ImageView leftImage;

    @FXML
    private ImageView rightImage;

    @FXML
    private TextField tfUsername;

    @FXML
    private PasswordField tfPassword;

    @FXML
    private Button btnLoginToPage;

    @FXML
    private Button btnLoginWGoogle;

    @FXML
    private Button btnRegisterPage;

    // @FXML
    // public void initialize() {
    // // Optional initialization logic
    // }

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = tfUsername.getText();
        String password = tfPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(AlertType.WARNING, "Username or Password doesnt fill ",
                    "Please fill Username and Password correctly");
            return;
        }

        UserServiceXStream userService = new UserServiceXStream();

        boolean loginResult = userService.loginUser(username, password);

        if (loginResult) {

            try {
                Parent homePage = FXMLLoader.load(getClass().getResource("/view/homeZen.fxml"));
                Scene scene = new Scene(homePage);

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
                stage.centerOnScreen();

                System.out.println("Login successful!");
                showAlert(AlertType.CONFIRMATION, "Welcome to Zenloop",
                        "Welcome to zenloop, before you start the application, would you like to aggre for the user policy?");

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error loading home page: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Invalid Data",
                    "Your username or password doesnt match, please fill the correct one");
        }
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        try {
            Parent registPage = FXMLLoader.load(getClass().getResource("/view/Register/registerFXML.fxml"));
            Scene scene = new Scene(registPage);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            stage.centerOnScreen();
        } catch (Exception e) {
            System.out.println("kesalahan dalam memuat halaman");
        }
    }

    @FXML
    public void handleGoogleLogin(ActionEvent event) {

        try {
            Parent registPage = FXMLLoader.load(getClass().getResource("/view/login/loginGoogle.fxml"));
            Scene scene = new Scene(registPage);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            stage.centerOnScreen();
        } catch (Exception e) {
            System.out.println("kesalahan dalam memuat halaman");
        }
    }

    @FXML
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
