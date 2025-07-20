package controller.sidebar;

import controller.HomeController;
import controller.homeDr.HomeDrController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.SessionManager;
import model.UserData;

public class SidebarDrController {

    @FXML
    private Button btnLogOut;
    @FXML
    private Button btnLogOutSidebarDr;

    @FXML
    private HBox homepageItemDr, jurnalArchiveItemDr, zenBotAiDr, yourClinic, settingDr, fAQ;
    @FXML
    private ImageView icon1Dr, icon2Dr, icon3Dr, icon4Dr, icon5Dr, icon6Dr;
    @FXML
    private Text homepageTextDr, jurnalArchiveTextDr, zenBotAitextDr, findYourKonselorTextDr, settingTextDr, fAQTextDr;
    @FXML
    private Circle imgProfileDr;
    @FXML
    private Label nameUserDr, zenloopCodeDr;

    private HBox activeItem = null;

    @FXML
    public void initialize() {
        System.out.println("Sidebar Dr loaded");

        if (SessionManager.isLoggedIn()) {
            UserData user = SessionManager.getCurrentUser();
            nameUserDr.setText(user.getUsername());
            zenloopCodeDr.setText(user.getEmail());
            setProfileImage(user.getProfileImagePath());

            System.out.println("Logged in as: " + user.getNama() + " | Email: " + user.getEmail());
        } else {
            nameUserDr.setText("Doctor Name");
            zenloopCodeDr.setText("Zenloop Email");
        }

        setActiveItem(homepageItemDr, icon1Dr, "/app/resource/icon1Act.png");
    }

    @FXML
    void handleHomepageDrClick(MouseEvent event) {
        setActiveItem(homepageItemDr, icon1Dr, "/app/resource/icon1Act.png");
        if (HomeDrController.getInstance() != null) {
            HomeDrController.getInstance().showHomepage();
        }
    }

    @FXML
    void hanldePatientDashboard(MouseEvent event) {
        setActiveItem(jurnalArchiveItemDr, icon2Dr, "/app/resource/icon2Act.png");
        if (HomeDrController.getInstance() != null) {
            HomeDrController.getInstance().showPatientDashboard();
        }
    }

    @FXML
    void handleClinic(MouseEvent event) {
        setActiveItem(yourClinic, icon3Dr, "/app/resource/icon5Act.png");
        if (HomeDrController.getInstance() != null) {
            HomeDrController.getInstance().showYourClinicDr();
        }
    }

    @FXML
    void handleZenbotAiDr(MouseEvent event) {
        setActiveItem(zenBotAiDr, icon4Dr, "/app/resource/icon4Act.png");
        if (HomeDrController.getInstance() != null) {
            HomeDrController.getInstance().showZenBotAi();
        }
    }

    @FXML
    void handleSettingClickDr(MouseEvent event) {
        setActiveItem(settingDr, icon5Dr, "/app/resource/icon9Act.png");
        if (HomeDrController.getInstance() != null) {
            HomeDrController.getInstance().showSetting();
        }
    }

    @FXML
    void handlefAQClick(MouseEvent event) {
        setActiveItem(fAQ, icon6Dr, "/app/resource/icon10Act.png");
        if (HomeDrController.getInstance() != null) {
            HomeDrController.getInstance().showFAQ();
        }
    }

    @FXML
    void handleLogoutSidebarDr(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText("Are You Sure?");
        alert.setContentText("Do you want to log out from Zenloop?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    Parent loginPage = FXMLLoader.load(getClass().getResource("/view/login/loginZenloopFXML.fxml"));
                    Scene scene = new Scene(loginPage);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                    stage.centerOnScreen();
                } catch (Exception e) {
                    System.err.println("Error loading login page: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    private void setActiveItem(HBox item, ImageView icon, String activeIconPath) {
        if (activeItem != null) {
            activeItem.getStyleClass().remove("active");
        }

        activeItem = item;
        if (!item.getStyleClass().contains("active")) {
            item.getStyleClass().add("active");
        }

        resetIcons();

        try {
            icon.setImage(new Image(getClass().getResourceAsStream(activeIconPath)));
        } catch (Exception e) {
            System.err.println("Failed to load active icon: " + activeIconPath);
            e.printStackTrace();
        }
    }

    private void setProfileImage(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                Image image = new Image(getClass().getResourceAsStream(imagePath));
                imgProfileDr.setFill(new javafx.scene.paint.ImagePattern(image));
            } catch (Exception e) {
                System.err.println("Failed to load profile image: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            // Default avatar jika imagePath null
            try {
                Image image = new Image(getClass().getResourceAsStream("/app/resource/defaultProfile.png"));
                imgProfileDr.setFill(new javafx.scene.paint.ImagePattern(image));
            } catch (Exception e) {
                System.err.println("Failed to load default profile image.");
            }
        }
    }

    private void resetIcons() {
        try {
            icon1Dr.setImage(new Image(getClass().getResourceAsStream("/app/resource/icon1.png")));
            icon2Dr.setImage(new Image(getClass().getResourceAsStream("/app/resource/icon2.png")));
            icon3Dr.setImage(new Image(getClass().getResourceAsStream("/app/resource/icon5.png")));
            icon4Dr.setImage(new Image(getClass().getResourceAsStream("/app/resource/icon4.png")));
            icon5Dr.setImage(new Image(getClass().getResourceAsStream("/app/resource/icon9.png")));
            icon6Dr.setImage(new Image(getClass().getResourceAsStream("/app/resource/icon10.png")));
        } catch (Exception e) {
            System.err.println("Failed to reset icons: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
