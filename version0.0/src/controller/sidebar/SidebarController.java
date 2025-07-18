package controller.sidebar;

import javax.swing.Action;

import controller.HomeController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.SessionManager;
import model.UserData;

public class SidebarController {

    @FXML
    private HBox homepageItem, jurnalArchiveItem, stresStatistic, zenBotAi, findYourKonselor,
            relaxMusic, community, history, setting, fAQ;

    @FXML
    private ImageView icon1, icon2, icon3, icon4, icon5, icon6, icon7, icon8, icon9, icon10;

    @FXML
    private Text homepageText, jurnalArchiveText, stresStatisticText, zenBotAitext,
            findYourKonselorText, relaxMusicText, communityText, historyText,
            settingText, fAQText;

    @FXML
    private Label nameUser;

    @FXML
    private Label zenloopEmail;

    private HBox activeItem = null;

    private static SidebarController instance;

    public static SidebarController getInstance() {
        return instance;
    }

    public void activateStressMenu() {
        setActiveItem(stresStatistic, icon3, "/app/resource/icon3Act.png");
    }

    public void activateJurnalMenu() {
        setActiveItem(jurnalArchiveItem, icon2, "/app/resource/icon2Act.png");
    }

    @FXML
    public void initialize() {
        instance = this;
        System.out.println("Sidebar loaded");

        if (SessionManager.isLoggedIn()) {
            UserData user = SessionManager.getCurrentUser();
            nameUser.setText(user.getUsername());
            // nameUser.setText(user.getNama());
            zenloopEmail.setText(user.getEmail());
            System.out.println("Logged in as: " + user.getNama() + " | Email: " + user.getEmail());
        } else {
            nameUser.setText("Your Name");
            zenloopEmail.setText("ZenEmail");
        }

        // Set default active item
        setActiveItem(homepageItem, icon1, "/app/resource/icon1Act.png");
    }

    @FXML
    public void handleHomepageClick() {
        setActiveItem(homepageItem, icon1, "/app/resource/icon1Act.png");
        // Navigate to homepage
        if (HomeController.getInstance() != null) {
            HomeController.getInstance().showHomepage();
        }
    }

    @FXML
    public void handleJurnalArchiveClick() {
        setActiveItem(jurnalArchiveItem, icon2, "/app/resource/icon2Act.png");
        // Navigate to jurnal archive
        if (HomeController.getInstance() != null) {
            HomeController.getInstance().showJurnalArchive();
        }
    }

    @FXML
    public void handleStresStatisticClick() {
        setActiveItem(stresStatistic, icon3, "/app/resource/icon3Act.png");
        // Navigate to stress statistic
        if (HomeController.getInstance() != null) {
            HomeController.getInstance().showStresStatistic();
        }
    }

    @FXML
    public void handleZenBotAiClick() {
        setActiveItem(zenBotAi, icon4, "/app/resource/icon4Act.png");
        // Navigate to ZenBot AI
        if (HomeController.getInstance() != null) {
            HomeController.getInstance().showZenBotAi();
        }
    }

    @FXML
    public void handleFindYourKonselorClick() {
        setActiveItem(findYourKonselor, icon5, "/app/resource/icon5Act.png");
        // Navigate to find konselor
        if (HomeController.getInstance() != null) {
            HomeController.getInstance().showFindYourKonselor();
        }
    }

    @FXML
    public void handleRelaxMusicClick() {
        setActiveItem(relaxMusic, icon6, "/app/resource/icon6Act.png");
        // Navigate to relax music
        if (HomeController.getInstance() != null) {
            HomeController.getInstance().showRelaxMusic();
        }
    }

    @FXML
    public void handleCommunityClick() {
        setActiveItem(community, icon7, "/app/resource/icon7ct.png");
        // Navigate to community
        if (HomeController.getInstance() != null) {
            HomeController.getInstance().showCommunity();
        }
    }

    @FXML
    public void handleHistoryClick() {
        setActiveItem(history, icon8, "/app/resource/icon8Act.png");
        // Navigate to history
        if (HomeController.getInstance() != null) {
            HomeController.getInstance().showHistory();
        }
    }

    @FXML
    public void handleSettingClick() {
        setActiveItem(setting, icon9, "/app/resource/icon9Act.png");
        // Navigate to setting
        if (HomeController.getInstance() != null) {
            HomeController.getInstance().showSetting();
        }
    }

    @FXML
    public void handlefAQClick() {
        setActiveItem(fAQ, icon10, "/app/resource/icon10Act.png");
        // Navigate to FAQ
        if (HomeController.getInstance() != null) {
            HomeController.getInstance().showFAQ();
        }
    }

    @FXML
    public void handleLogoutSidebar(ActionEvent event) {
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
            icon1.setImage(new Image(getClass().getResourceAsStream("/app/resource/icon1.png")));
            icon2.setImage(new Image(getClass().getResourceAsStream("/app/resource/icon2.png")));
            icon3.setImage(new Image(getClass().getResourceAsStream("/app/resource/icon3.png")));
            icon4.setImage(new Image(getClass().getResourceAsStream("/app/resource/icon4.png")));
            icon5.setImage(new Image(getClass().getResourceAsStream("/app/resource/icon5.png")));
            icon6.setImage(new Image(getClass().getResourceAsStream("/app/resource/icon6.png")));
            icon7.setImage(new Image(getClass().getResourceAsStream("/app/resource/icon7.png")));
            icon8.setImage(new Image(getClass().getResourceAsStream("/app/resource/icon8.png")));
            icon9.setImage(new Image(getClass().getResourceAsStream("/app/resource/icon9.png")));
            icon10.setImage(new Image(getClass().getResourceAsStream("/app/resource/icon10.png")));
        } catch (Exception e) {
            System.err.println("Error resetting icons: " + e.getMessage());
            e.printStackTrace();
        }
    }

}