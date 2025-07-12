package controller.sidebar;

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
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SidebarDrController {

    @FXML
    private HBox homepageItemDr, jurnalArchiveItemDr, findYourKonselorDr, zenBotAiDr, settingDr, fAQ;

    @FXML
    private ImageView icon1Dr, icon2Dr, icon3Dr, icon4Dr, icon5Dr, icon6Dr;

    @FXML
    private Text homepageTextDr, jurnalArchiveTextDr, findYourKonselorTextDr, zenBotAitextDr,
            settingTextDr, fAQTextDr;

    @FXML
    private Button btnLogOut, btnLogOutSidebarDr;

    @FXML
    private Label nameUserDr, zenloopCodeDr;

    @FXML
    private Circle imgProfileDr;

    private HBox activeItem = null;
    private static SidebarDrController instance;

    public static SidebarDrController getInstance() {
        return instance;
    }

    @FXML
    public void initialize() {
        instance = this;
        System.out.println("SidebarDr loaded");
        // Set default active item
        setActiveItem(homepageItemDr, icon1Dr, "/app/resource/icon1Act.png");
    }

    @FXML
    public void handleHomepageClick(ActionEvent event) {
        setActiveItem(homepageItemDr, icon1Dr, "/app/resource/icon1Act.png");
        // Navigate to homepage
        if (HomeDrController.getInstance() != null) {
            HomeDrController.getInstance().showHomepage();
        }
    }

    @FXML
    public void handleJurnalArchiveClick(ActionEvent event) {
        setActiveItem(jurnalArchiveItemDr, icon2Dr, "/app/resource/icon2Act.png");
        // Navigate to jurnal archive
        if (HomeDrController.getInstance() != null) {
            HomeDrController.getInstance().showJurnalArchive();
        }
    }

    @FXML
    public void handleFindYourKonselorClick(ActionEvent event) {
        setActiveItem(findYourKonselorDr, icon3Dr, "/app/resource/icon3Act.png");
        // Navigate to find konselor
        if (HomeDrController.getInstance() != null) {
            HomeDrController.getInstance().showFindYourKonselor();
        }
    }

    @FXML
    public void handleZenBotAiClick(ActionEvent event) {
        setActiveItem(zenBotAiDr, icon4Dr, "/app/resource/icon4Act.png");
        // Navigate to ZenBot AI
        if (HomeDrController.getInstance() != null) {
            HomeDrController.getInstance().showZenBotAi();
        }
    }

    @FXML
    public void handleSettingClick(ActionEvent event) {
        setActiveItem(settingDr, icon5Dr, "/app/resource/icon5Act.png");
        // Navigate to setting
        if (HomeDrController.getInstance() != null) {
            HomeDrController.getInstance().showSetting();
        }
    }

    @FXML
    public void handlefAQClick(ActionEvent event) {
        setActiveItem(fAQ, icon6Dr, "/app/resource/icon6Act.png");
        // Navigate to FAQ
        if (HomeDrController.getInstance() != null) {
            HomeDrController.getInstance().showFAQ();
        }
    }

    @FXML
    public void handleLogoutSidebarDr(ActionEvent event) {
        try {
            // Show confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Logout Confirmation");
            alert.setHeaderText("Are You Sure?");
            alert.setContentText("Do you want to log out from Zenloop?");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        // Load login page
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
        } catch (Exception e) {
            System.err.println("Error in logout process: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Public methods for programmatic menu activation
    public void activateHomepage() {
        setActiveItem(homepageItemDr, icon1Dr, "/app/resource/icon1Act.png");
    }

    public void activateJurnalMenu() {
        setActiveItem(jurnalArchiveItemDr, icon2Dr, "/app/resource/icon2Act.png");
    }

    public void activateFindKonselorMenu() {
        setActiveItem(findYourKonselorDr, icon3Dr, "/app/resource/icon3Act.png");
    }

    private void setActiveItem(HBox item, ImageView icon, String activeIconPath) {
        // Remove active class from previous item
        if (activeItem != null) {
            activeItem.getStyleClass().remove("active");
        }

        // Set new active item
        activeItem = item;
        if (!item.getStyleClass().contains("active")) {
            item.getStyleClass().add("active");
        }

        // Reset all icons to default
        resetIcons();

        // Set active icon
        try {
            icon.setImage(new Image(getClass().getResourceAsStream(activeIconPath)));
        } catch (Exception e) {
            System.err.println("Error loading active icon: " + activeIconPath);
            e.printStackTrace();
        }
    }

    private void resetIcons() {
        try {
            icon1Dr.setImage(new Image(getClass().getResourceAsStream("/app/resource/icon1.png")));
            icon2Dr.setImage(new Image(getClass().getResourceAsStream("/app/resource/icon2.png")));
            icon3Dr.setImage(new Image(getClass().getResourceAsStream("/app/resource/icon3.png")));
            icon4Dr.setImage(new Image(getClass().getResourceAsStream("/app/resource/icon4.png")));
            icon5Dr.setImage(new Image(getClass().getResourceAsStream("/app/resource/icon5.png")));
            icon6Dr.setImage(new Image(getClass().getResourceAsStream("/app/resource/icon6.png")));
        } catch (Exception e) {
            System.err.println("Error resetting icons: " + e.getMessage());
            e.printStackTrace();
        }
    }
}