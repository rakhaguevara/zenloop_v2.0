package controller;

import javax.swing.Action;

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

public class SidebarController {

    @FXML
    private HBox homepageItem;
    @FXML
    private HBox jurnalArchiveItem;
    @FXML
    private HBox stresStatistic;
    @FXML
    private HBox zenBotAi;
    @FXML
    private HBox findYourKonselor;
    @FXML
    private HBox relaxMusic;
    @FXML
    private HBox community;
    @FXML
    private HBox history;
    @FXML
    private HBox setting;
    @FXML
    private HBox fAQ;

    @FXML
    private ImageView icon1;
    @FXML
    private ImageView icon2;
    @FXML
    private ImageView icon3;
    @FXML
    private ImageView icon4;
    @FXML
    private ImageView icon5;
    @FXML
    private ImageView icon6;
    @FXML
    private ImageView icon7;
    @FXML
    private ImageView icon8;
    @FXML
    private ImageView icon9;
    @FXML
    private ImageView icon10;

    @FXML
    private Text homepageText;
    @FXML
    private Text jurnalArchiveText;
    @FXML
    private Text stresStatisticText;
    @FXML
    private Text zenBotAitext;
    @FXML
    private Text findYourKonselorText;
    @FXML
    private Text relaxMusicText;
    @FXML
    private Text communityText;
    @FXML
    private Text historyText;
    @FXML
    private Text settingText;
    @FXML
    private Text fAQText;

    private HBox activeItem = null;

    @FXML
    public void initialize() {
        System.out.println("Sidebar loaded");
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