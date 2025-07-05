package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

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
        // Atur item default yang aktif jika perlu
        setActiveItem(homepageItem, icon1, "/app/resource/icon1Act.png");
    }

    @FXML
    public void handleHomepageClick() {
        setActiveItem(homepageItem, icon1, "/app/resource/icon1Act.png");
    }

    @FXML
    public void handleJurnalArchiveClick() {
        setActiveItem(jurnalArchiveItem, icon2, "/app/resource/icon2Act.png");
    }

    @FXML
    public void handleStresStatisticClick() {
        setActiveItem(stresStatistic, icon3, "/app/resource/icon3Act.png");
    }

    @FXML
    public void handleZenBotAiClick() {
        setActiveItem(zenBotAi, icon4, "/app/resource/icon4Act.png");
    }

    @FXML
    public void handleFindYourKonselorClick() {
        setActiveItem(findYourKonselor, icon5, "/app/resource/icon5Act.png");
    }

    @FXML
    public void handleRelaxMusicClick() {
        setActiveItem(relaxMusic, icon6, "/app/resource/icon6Act.png");
    }

    @FXML
    public void handleCommunityClick() {
        setActiveItem(community, icon7, "/app/resource/icon7ct.png");
    }

    @FXML
    public void handleHistoryClick() {
        setActiveItem(history, icon8, "/app/resource/icon8Act.png");
    }

    @FXML
    public void handleSettingClick() {
        setActiveItem(setting, icon9, "/app/resource/icon9Act.png");
    }

    @FXML
    public void handlefAQClick() {
        setActiveItem(fAQ, icon10, "/app/resource/icon10Act.png");
    }

    private void setActiveItem(HBox item, ImageView icon, String activeIconPath) {
        if (activeItem != null) {
            activeItem.getStyleClass().remove("active");
        }

        activeItem = item;
        if (!item.getStyleClass().contains("active")) {
            item.getStyleClass().add("active");
        }

        // Reset semua icon
        resetIcons();

        // Set icon aktif
        icon.setImage(new Image(getClass().getResourceAsStream(activeIconPath)));
    }

    private void resetIcons() {
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

        // Tambahkan semua icon lainnya di sini
    }
}
