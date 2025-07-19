package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import java.io.IOException;

import controller.routingControll.PageConfig;

public class HomeController {

    @FXML
    private AnchorPane mainContentPane;

    @FXML
    private VBox rightBarPane;

    @FXML
    private BarChart<String, Number> tvStress;

    private static BarChart<String, Number> staticTvStress;

    private static HomeController instance;
    private PageConfig currentPage;

    public HomeController() {
        instance = this;
    }

    public static HomeController getInstance() {
        return instance;
    }

    @FXML
    public void initialize() {
        System.out.println("HomeController initialized");
        // Load default page (homepage)
        loadPage(PageConfig.HOMEPAGE);
        staticTvStress = tvStress;
    }

    // Method to load different pages using PageConfig enum
    public void loadPage(PageConfig pageConfig) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(pageConfig.getFxmlPath()));
            Parent page = loader.load();

            // Clear current content and add new page
            mainContentPane.getChildren().clear();
            mainContentPane.getChildren().add(page);

            // Make sure the page fills the entire pane
            AnchorPane.setTopAnchor(page, 0.0);
            AnchorPane.setBottomAnchor(page, 0.0);
            AnchorPane.setLeftAnchor(page, 0.0);
            AnchorPane.setRightAnchor(page, 0.0);

            // Control rightbar visibility based on page configuration
            setRightbarVisibility(pageConfig.shouldShowRightbar());

            // Update current page
            currentPage = pageConfig;

        } catch (IOException e) {
            System.err.println("Error loading page: " + pageConfig.getFxmlPath());
            e.printStackTrace();
        }
    }

    // Overloaded method for backward compatibility
    public void loadPage(String fxmlPath) {
        PageConfig config = PageConfig.getByPath(fxmlPath);
        if (config != null) {
            loadPage(config);
        } else {
            // Fallback for custom pages not in enum
            loadCustomPage(fxmlPath, true); // Default to show rightbar
        }
    }

    // Method for loading custom pages not in enum
    private void loadCustomPage(String fxmlPath, boolean showRightbar) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent page = loader.load();

            mainContentPane.getChildren().clear();
            mainContentPane.getChildren().add(page);

            AnchorPane.setTopAnchor(page, 0.0);
            AnchorPane.setBottomAnchor(page, 0.0);
            AnchorPane.setLeftAnchor(page, 0.0);
            AnchorPane.setRightAnchor(page, 0.0);

            setRightbarVisibility(showRightbar);

        } catch (IOException e) {
            System.err.println("Error loading custom page: " + fxmlPath);
            e.printStackTrace();
        }
    }

    public static BarChart<String, Number> getTvStress() {
        return staticTvStress;
    }

    // Method to control rightbar visibility
    private void setRightbarVisibility(boolean shouldShow) {
        if (rightBarPane != null) {
            rightBarPane.setVisible(shouldShow);
            rightBarPane.setManaged(shouldShow);
        }
    }

    // Convenient navigation methods using enum
    public void showHomepage() {
        loadPage(PageConfig.HOMEPAGE);
    }

    public void showJurnalArchive() {
        loadPage(PageConfig.JURNAL_ARCHIVE);
    }

    public void showStresStatistic() {
        loadPage(PageConfig.STRES_STATISTIC);
    }

    public void showZenBotAi() {
        loadPage(PageConfig.ZEN_BOT_AI);
    }

    public void showFindYourKonselor() {
        loadPage(PageConfig.FIND_KONSELOR);
    }

    public void showRelaxMusic() {
        loadPage(PageConfig.RELAX_MUSIC);
    }

    public void showCommunity() {
        loadPage(PageConfig.COMMUNITY);
    }

    public void showHistory() {
        loadPage(PageConfig.HISTORY);
    }

    public void showSetting() {
        loadPage(PageConfig.SETTING); // This will hide rightbar
    }

    public void showFAQ() {
        loadPage(PageConfig.FAQ);
    }

    // Utility methods for rightbar control
    public void hideRightbar() {
        setRightbarVisibility(false);
    }

    public void showRightbar() {
        setRightbarVisibility(true);
    }

    public void toggleRightbar() {
        if (rightBarPane != null) {
            boolean isVisible = rightBarPane.isVisible();
            setRightbarVisibility(!isVisible);
        }
    }

    // Method to get current page info
    public PageConfig getCurrentPage() {
        return currentPage;
    }

    public boolean isRightbarVisible() {
        return rightBarPane != null && rightBarPane.isVisible();
    }

    public AnchorPane getMainContentPane() {
        return mainContentPane;
    }
}