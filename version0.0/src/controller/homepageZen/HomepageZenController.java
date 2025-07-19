package controller.homepageZen;

import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import controller.HomeController;
import controller.sidebar.SidebarController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import model.StressHarian;

public class HomepageZenController {

    @FXML
    private Button btnToStressCheck;

    @FXML
    private Button btnToJournal;

    @FXML
    private Button txtMarkComplete, txtMarkComplete1, txtMarkComplete2;

    @FXML
    private LineChart<String, Number> tvStress;

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
    public void handleToJournal(ActionEvent event) {
        SidebarController sidebar = SidebarController.getInstance();

        if (sidebar != null) {
            sidebar.activateJurnalMenu();
        }

        if (HomeController.getInstance() != null) {
            HomeController.getInstance().showJurnalArchive();
        }
    }

    @FXML
    private void handleCompleteAction(ActionEvent event) {
        markAsComplete();
    }

    private void muatDariXML() {
        try {
            String username = model.SessionManager.getCurrentUser().getUsername();
            String path = "data_stress_tabel_" + username + ".xml";

            FileReader reader = new FileReader(path);
            XStream xstream = new XStream(new StaxDriver());
            xstream.allowTypes(new Class[] { StressHarian.class, ArrayList.class });
            xstream.alias("stress", StressHarian.class);
            xstream.allowTypesByWildcard(new String[] { "model.**" });

            List<StressHarian> list = (List<StressHarian>) xstream.fromXML(reader);

            // Tampilkan rata-rata per minggu
            refreshLineChartPerMinggu(list);
        } catch (Exception e) {
            System.out.println("⚠️ Gagal memuat XML stress user di homepage: " + e.getMessage());
        }
    }

    private void markAsComplete() {
        if ("Mark Complete".equals(txtMarkComplete.getText())) {
            txtMarkComplete.setText("Complete");
            txtMarkComplete.setStyle("-fx-text-fill: #015C55; -fx-font-weight: bold;");
        } else {
            txtMarkComplete.setText("Mark Complete");
            txtMarkComplete.setStyle("-fx-text-fill: black;");
        }
    }

    private void refreshLineChartPerMinggu(List<StressHarian> riwayatData) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Rata-Rata Mingguan");

        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM", Locale.getDefault());

        Map<LocalDate, List<Double>> dataPerMinggu = new TreeMap<>(); // key = tanggal Senin (awal minggu)

        for (StressHarian data : riwayatData) {
            LocalDate tanggal = LocalDate.parse(data.getTanggal());
            LocalDate startOfWeek = tanggal.with(weekFields.dayOfWeek(), 1); // Senin
            dataPerMinggu.putIfAbsent(startOfWeek, new ArrayList<>());
            dataPerMinggu.get(startOfWeek).add(data.getRataRata());
        }

        // Ambil 4 minggu terakhir
        List<LocalDate> mingguTerakhir = dataPerMinggu.keySet().stream()
                .sorted(Comparator.reverseOrder())
                .limit(4)
                .sorted()
                .collect(java.util.stream.Collectors.toList());

        for (LocalDate startOfWeek : mingguTerakhir) {
            List<Double> nilai = dataPerMinggu.get(startOfWeek);
            double rata2 = nilai.stream().mapToDouble(Double::doubleValue).average().orElse(0);

            LocalDate endOfWeek = startOfWeek.plusDays(6); // Minggu
            String label = String.format("Minggu (%s - %s)",
                    startOfWeek.format(formatter),
                    endOfWeek.format(formatter));

            series.getData().add(new XYChart.Data<>(label, rata2));
        }

        tvStress.getData().clear();
        tvStress.getData().add(series);
    }

}
