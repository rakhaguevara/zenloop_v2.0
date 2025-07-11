package controller.homepageZen;

import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import controller.HomeController;
import controller.sidebar.SidebarController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import model.StressHarian;

public class HomepageZenController {

    @FXML
    private Button btnToStressCheck;

    @FXML
    private Button btnToJournal;

    @FXML
    private BarChart<String, Number> tvStress;

    @FXML
    private AnchorPane homePage;

    @FXML
    public void initialize() {
        muatDariXML();
    }

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

    private void muatDariXML() {
        try (FileReader reader = new FileReader("data_stress_tabel.xml")) {
            XStream xstream = new XStream(new StaxDriver());
            xstream.allowTypes(new Class[] { StressHarian.class, ArrayList.class });
            List<StressHarian> list = (List<StressHarian>) xstream.fromXML(reader);

            // Menggunakan data dari XML untuk memperbarui BarChart
            refreshBarChart(list);
        } catch (Exception e) {
            System.out.println("Gagal memuat XML: " + e.getMessage());
        }
    }

    private void refreshBarChart(List<StressHarian> riwayatData) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E, dd MMM", Locale.forLanguageTag("id")); // Format
                                                                                                             // Indonesia

        for (StressHarian data : riwayatData) {
            LocalDate tanggal = LocalDate.parse(data.getTanggal()); // asumsikan "yyyy-MM-dd"
            String labelHari = tanggal.format(formatter); // misalnya "Kam, 11 Jul"

            series.getData().add(new XYChart.Data<>(labelHari, data.getRataRata()));
        }

        tvStress.getData().clear();
        tvStress.getData().add(series);
    }

}
