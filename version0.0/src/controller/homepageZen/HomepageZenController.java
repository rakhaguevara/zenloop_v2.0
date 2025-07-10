package controller.homepageZen;

import controller.HomeController;
import controller.sidebar.SidebarController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class HomepageZenController {

    @FXML
    private Button btnToStressCheck;

    @FXML
    private Button btnToJournal;

    @FXML
    private BarChart<?, ?> tvStress;

    @FXML
    private AnchorPane homePage;

    @FXML
    public void handleToStress(ActionEvent event) {
        SidebarController sidebar = SidebarController.getInstance();

        if (sidebar != null) {
            sidebar.activateStressMenu();
        }

        if (HomeController.getInstance() != null) {
            HomeController.getInstance().showStresStatistic();
        }
    }

    @FXML
    public void handleToJournal(ActionEvent evnt) {
        SidebarController sidebar = SidebarController.getInstance();

        if (sidebar != null) {
            sidebar.activateJurnalMenu();
        }
        if (HomeController.getInstance() != null) {
            HomeController.getInstance().showJurnalArchive();
        }
    }

}
