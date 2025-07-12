package controller.homeDr;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import java.io.IOException;

import controller.routingControll.PageConfigDr;

public class HomeDrController {

    @FXML
    private AnchorPane mainContentPaneDr;

    @FXML
    private VBox rightBarInclude;

    private VBox rightBarPaneDr;

    private static HomeDrController instance;
    private PageConfigDr currentPage;

    public HomeDrController() {
        instance = this;
    }

    public static HomeDrController getInstance() {
        return instance;
    }

    @FXML
    public void initialize() {
        System.out.println("HomeDrController initialized");
        // Load default page (homepage)
        loadPage(PageConfigDr.HOMEPAGE_DR);
    }

    // Method to load different pages using PageConfigDr enum
    public void loadPage(PageConfigDr pageConfig) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(pageConfig.getFxmlPath()));
            Parent page = loader.load();

            // Clear current content and add new page
            mainContentPaneDr.getChildren().clear();
            mainContentPaneDr.getChildren().add(page);

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
        PageConfigDr config = PageConfigDr.getByPath(fxmlPath);
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

            mainContentPaneDr.getChildren().clear();
            mainContentPaneDr.getChildren().add(page);

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

    // Method to control rightbar visibility
    private void setRightbarVisibility(boolean shouldShow) {
        if (rightBarPaneDr != null) {
            rightBarPaneDr.setVisible(shouldShow);
            rightBarPaneDr.setManaged(shouldShow);
        }
    }

    // Convenient navigation methods using enum
    public void showHomepage() {
        loadPage(PageConfigDr.HOMEPAGE_DR);
    }

    public void showPatientDashboard() {
        loadPage(PageConfigDr.PATIENT_DASHBOARD_DR);
    }

    public void showYourClinicDr() {
        loadPage(PageConfigDr.YOUR_CLINIC);
    }

    public void showZenBotAi() {
        loadPage(PageConfigDr.ZEN_BOT_AI_DR);
    }

    public void showSetting() {
        loadPage(PageConfigDr.SETTING_DR);
    }

    public void showFAQ() {
        loadPage(PageConfigDr.FAQ_DR);
    }

    // Utility methods for rightbar control
    public void hideRightbar() {
        setRightbarVisibility(false);
    }

    public void showRightbar() {
        setRightbarVisibility(true);
    }

    public void toggleRightbar() {
        if (rightBarPaneDr != null) {
            boolean isVisible = rightBarPaneDr.isVisible();
            setRightbarVisibility(!isVisible);
        }
    }

    // Method to get current page info
    public PageConfigDr getCurrentPage() {
        return currentPage;
    }

    public boolean isRightbarVisible() {
        return rightBarPaneDr != null && rightBarPaneDr.isVisible();
    }

    public AnchorPane getMainContentPane() {
        return mainContentPaneDr;
    }
}