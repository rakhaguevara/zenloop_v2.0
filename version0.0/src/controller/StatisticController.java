package controller;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import model.StressHarian;
import util.AlertUtil;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class StatisticController {

    @FXML
    private LineChart<String, Number> lineChartStress;

    @FXML
    private CategoryAxis hariAxis;
    @FXML
    private NumberAxis nilaiAxis;

    @FXML
    private TableView<StressHarian> tabelStressStatistic;
    @FXML
    private TableColumn<StressHarian, String> kolomTanggal;
    @FXML
    private TableColumn<StressHarian, Double> kolomRataRata;
    @FXML
    private TableColumn<StressHarian, String> kolomKeterangan;

    @FXML
    private TextField tfTest1, tfTest2, tfTest3, tfTest4, tfTest5;
    @FXML
    private Button btnSaveStatistic, btnEditStatistic, btnDeleteData;
    @FXML
    private DatePicker datePicker;

    // private XYChart.Series<String, Number> dataMingguan = new XYChart.Series<>();
    private ObservableList<StressHarian> riwayatData = FXCollections.observableArrayList();
    private final String[] hariMinggu = { "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu" };

    @FXML
    public void initialize() {
        if (hariAxis != null) {
            hariAxis.setCategories(FXCollections.observableArrayList(Arrays.asList(hariMinggu)));
            // lineChartStress.getData().add(dataMingguan);
        }

        kolomTanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        kolomRataRata.setCellValueFactory(new PropertyValueFactory<>("rataRata"));
        kolomKeterangan.setCellValueFactory(new PropertyValueFactory<>("keterangan"));
        tabelStressStatistic.setItems(riwayatData);

        tabelStressStatistic.setOnMouseClicked(event -> {
            StressHarian selected = tabelStressStatistic.getSelectionModel().getSelectedItem();
            if (selected != null) {
                tfTest1.setText(String.valueOf(selected.getQuest1()));
                tfTest2.setText(String.valueOf(selected.getQuest2()));
                tfTest3.setText(String.valueOf(selected.getQuest3()));
                tfTest4.setText(String.valueOf(selected.getQuest4()));
                datePicker.setValue(LocalDate.parse(selected.getTanggal()));
            }
        });

        muatDariXML();
    }

    @FXML
    void handleSaveStatistic(ActionEvent event) {
        try {
            int quest1 = Integer.parseInt(tfTest1.getText());
            int quest2 = Integer.parseInt(tfTest2.getText());
            int quest3 = Integer.parseInt(tfTest3.getText());
            int quest4 = Integer.parseInt(tfTest4.getText());

            if (quest1 < 0 || quest1 > 10 || quest2 < 0 || quest2 > 10 ||
                    quest3 < 0 || quest3 > 10 || quest4 < 0 || quest4 > 10) {
                AlertUtil.showAlert(Alert.AlertType.WARNING, "Validasi", "Nilai harus antara 0 sampai 10.");
                return;
            }

            LocalDate tanggal = datePicker.getValue();
            if (tanggal == null) {
                AlertUtil.showAlert(Alert.AlertType.WARNING, "Validasi", "Silakan pilih tanggal terlebih dahulu.");
                return;
            }

            if (isTanggalExist(tanggal)) {
                AlertUtil.showAlert(Alert.AlertType.WARNING, "Validasi", "Tanggal sudah ada. Gunakan tombol edit.");
                return;
            }

            double rata2 = (quest1 + quest2 + quest3 + quest4) / 4.0;
            String keterangan = getKeterangan(rata2);

            riwayatData.add(
                    new StressHarian(tanggal.toString(), rata2, keterangan, quest1, quest2, quest3, quest4));
            tabelStressStatistic.refresh();
            refreshLineChart();
            simpanKeXML();
            AlertUtil.showAlert(AlertType.CONFIRMATION, "Berhasil", "Data berhasil disimpan");
            clearFields();

        } catch (NumberFormatException e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", "Masukkan angka valid.");
        }
    }

    @FXML
    void handleEditStatistic(ActionEvent event) {
        StressHarian selected = tabelStressStatistic.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtil.showAlert(Alert.AlertType.WARNING, "Peringatan", "Pilih data yang ingin diedit.");
            return;
        }

        try {
            int quest1 = Integer.parseInt(tfTest1.getText());
            int quest2 = Integer.parseInt(tfTest2.getText());
            int quest3 = Integer.parseInt(tfTest3.getText());
            int quest4 = Integer.parseInt(tfTest4.getText());
            LocalDate tanggal = datePicker.getValue();

            double rata2 = (quest1 + quest2 + quest3 + quest4) / 4.0;
            String keterangan = getKeterangan(rata2);

            selected.setQuest1(quest1);
            selected.setQuest2(quest2);
            selected.setQuest3(quest3);
            selected.setQuest4(quest4);
            selected.setTanggal(tanggal.toString());
            selected.setRataRata(rata2);
            selected.setKeterangan(keterangan);

            tabelStressStatistic.refresh();
            refreshLineChart();
            simpanKeXML();

            clearFields();

        } catch (NumberFormatException e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Error", "Masukkan angka valid.");
        }
    }

    @FXML
    void handleDeleteData(ActionEvent event) {
        StressHarian selected = tabelStressStatistic.getSelectionModel().getSelectedItem();
        if (selected != null) {
            riwayatData.remove(selected);
            refreshLineChart();
            simpanKeXML();
            AlertUtil.showAlert(AlertType.CONFIRMATION, null, "Data berhasil dihapus");
            clearFields();
        } else {
            AlertUtil.showAlert(Alert.AlertType.WARNING, "Peringatan", "Pilih data yang ingin dihapus.");
        }
    }

    private void refreshLineChart() {
        lineChartStress.getData().clear(); // Bersihkan seluruh data

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Rata-rata Stres per Hari");

        // Buat map hari -> daftar nilai
        Map<String, List<Double>> stressMap = new LinkedHashMap<>();
        for (String hari : hariMinggu) {
            stressMap.put(hari, new ArrayList<>());
        }

        for (StressHarian data : riwayatData) {
            LocalDate date = LocalDate.parse(data.getTanggal());
            String hari = hariMinggu[date.getDayOfWeek().getValue() - 1];
            stressMap.get(hari).add(data.getRataRata());
        }

        for (String hari : hariMinggu) {
            List<Double> nilai = stressMap.get(hari);
            if (!nilai.isEmpty()) {
                double avg = nilai.stream().mapToDouble(Double::doubleValue).average().orElse(0);
                series.getData().add(new XYChart.Data<>(hari, avg));
            } else {
                series.getData().add(new XYChart.Data<>(hari, 0)); // opsional: tampilkan 0 jika tidak ada data
            }
        }

        lineChartStress.getData().add(series);
    }

    private boolean isTanggalExist(LocalDate tanggal) {
        return riwayatData.stream().anyMatch(d -> d.getTanggal().equals(tanggal.toString()));
    }

    private String getKeterangan(double rata2) {
        if (rata2 >= 8)
            return "Hari Baik";
        if (rata2 >= 5)
            return "Lumayan";
        return "Hari Buruk";
    }

    private void simpanKeXML() {
        try {
            String username = model.SessionManager.getCurrentUser().getUsername();
            String path = "data_stress_tabel_" + username + ".xml";
            FileWriter writer = new FileWriter(path);
            XStream xstream = new XStream(new DomDriver());

            xstream.alias("stress", StressHarian.class);
            xstream.allowTypesByWildcard(new String[] { "model.**" });

            String xml = xstream.toXML(new ArrayList<>(riwayatData));
            writer.write(xml);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void muatDariXML() {
        try {
            String username = model.SessionManager.getCurrentUser().getUsername();
            String path = "data_stress_tabel_" + username + ".xml";
            File file = new File(path);
            if (!file.exists())
                return;

            FileReader reader = new FileReader(file);
            XStream xstream = new XStream(new StaxDriver());
            xstream.alias("stress", StressHarian.class);
            xstream.allowTypesByWildcard(new String[] { "model.**" });

            List<StressHarian> list = (List<StressHarian>) xstream.fromXML(reader);
            riwayatData.setAll(list);
            refreshLineChart();
        } catch (Exception e) {
            System.out.println("⚠️ Gagal memuat XML stress user: " + e.getMessage());
        }
    }

    private void clearFields() {
        tfTest1.clear();
        tfTest2.clear();
        tfTest3.clear();
        tfTest4.clear();
        datePicker.setValue(null);
    }
}
