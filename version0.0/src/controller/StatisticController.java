package controller;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import model.StressHarian;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class StatisticController {

    @FXML
    private BarChart<String, Number> barChartStress;
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

    private XYChart.Series<String, Number> dataMingguan = new XYChart.Series<>();
    private ObservableList<StressHarian> riwayatData = FXCollections.observableArrayList();
    private final String[] hariMinggu = { "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu" };

    @FXML
    public void initialize() {
        if (hariAxis != null) {
            hariAxis.setCategories(FXCollections.observableArrayList(Arrays.asList(hariMinggu)));
            barChartStress.getData().add(dataMingguan);
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
                tfTest5.setText(String.valueOf(selected.getQuest5()));
                datePicker.setValue(LocalDate.parse(selected.getTanggal()));
            }

        });

        muatDariXML();
    }

    @FXML
    void handleSaveStatistic(ActionEvent event) {
        try {
            // Validasi dan parsing input secara langsung
            int quest1 = Integer.parseInt(tfTest1.getText());
            int quest2 = Integer.parseInt(tfTest2.getText());
            int quest3 = Integer.parseInt(tfTest3.getText());
            int quest4 = Integer.parseInt(tfTest4.getText());
            int quest5 = Integer.parseInt(tfTest5.getText());

            // Memastikan nilai valid
            if (quest1 < 0 || quest1 > 10 || quest2 < 0 || quest2 > 10 ||
                    quest3 < 0 || quest3 > 10 || quest4 < 0 || quest4 > 10 ||
                    quest5 < 0 || quest5 > 10) {
                showAlert("Nilai harus antara 0 sampai 10.");
                return;
            }

            // Memastikan tanggal dipilih
            LocalDate tanggal = datePicker.getValue();
            if (tanggal == null) {
                showAlert("Silakan pilih tanggal terlebih dahulu.");
                return;
            }

            // Cek apakah tanggal sudah ada
            if (isTanggalExist(tanggal)) {
                showAlert("Tanggal sudah ada. Gunakan tombol edit.");
                return;
            }

            // Menghitung rata-rata dan keterangan
            double rata2 = (quest1 + quest2 + quest3 + quest4 + quest5) / 5.0;
            String keterangan = getKeterangan(rata2);

            // Menambah data ke riwayat dan memperbarui UI
            riwayatData.add(
                    new StressHarian(tanggal.toString(), rata2, keterangan, quest1, quest2, quest3, quest4, quest5));
            tabelStressStatistic.refresh();
            refreshBarChart();
            simpanKeXML();
            showAlert("Data berhasil disimpan.");
            clearFields();

        } catch (NumberFormatException e) {
            showAlert("Masukkan angka valid.");
        }
    }

    @FXML
    void handleEditStatistic(ActionEvent event) {
        StressHarian selected = tabelStressStatistic.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Pilih data yang ingin diedit.");
            return;
        }

        try {
            int quest1 = Integer.parseInt(tfTest1.getText());
            int quest2 = Integer.parseInt(tfTest2.getText());
            int quest3 = Integer.parseInt(tfTest3.getText());
            int quest4 = Integer.parseInt(tfTest4.getText());
            int quest5 = Integer.parseInt(tfTest5.getText());

            LocalDate tanggal = datePicker.getValue();

            double rata2 = (quest1 + quest2 + quest3 + quest4 + quest5) / 5.0;
            String keterangan = getKeterangan(rata2);

            selected.setQuest1(quest1);
            selected.setQuest2(quest2);
            selected.setQuest3(quest3);
            selected.setQuest4(quest4);
            selected.setQuest5(quest5);
            selected.setTanggal(tanggal.toString());
            selected.setRataRata(rata2);
            selected.setKeterangan(keterangan);

            tabelStressStatistic.refresh();
            refreshBarChart();
            simpanKeXML();
            showAlert("Data berhasil diperbarui.");
            clearFields();

        } catch (NumberFormatException e) {
            showAlert("Masukkan angka valid.");
        }
    }

    @FXML
    void handleDeleteData(ActionEvent event) {
        StressHarian selected = tabelStressStatistic.getSelectionModel().getSelectedItem();
        if (selected != null) {
            riwayatData.remove(selected);
            refreshBarChart();
            simpanKeXML();
            showAlert("Data berhasil dihapus.");

            clearFields();
        } else {
            showAlert("Pilih data yang ingin dihapus.");
        }

    }

    private void refreshBarChart() {
        dataMingguan.getData().clear();
        for (StressHarian data : riwayatData) {
            LocalDate date = LocalDate.parse(data.getTanggal());
            String hari = hariMinggu[date.getDayOfWeek().getValue() - 1];
            dataMingguan.getData().add(new XYChart.Data<>(hari, data.getRataRata()));
        }
        barChartStress.layout();
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
            String path = "data_stress_tabel.xml";
            File file = new File(path);

            // Periksa apakah file sudah ada
            if (!file.exists()) {
                // Jika file tidak ada, buat file baru
                file.createNewFile();
            }

            // Tulis data ke file XML
            FileWriter writer = new FileWriter(path);
            XStream xstream = new XStream(new StaxDriver());
            String xml = xstream.toXML(new ArrayList<>(riwayatData));
            writer.write(xml);
            writer.close();

            System.out.println("XML berhasil disimpan di: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void muatDariXML() {
        try (FileReader reader = new FileReader("data_stress_tabel.xml")) {
            XStream xstream = new XStream(new StaxDriver());
            xstream.allowTypes(new Class[] { StressHarian.class, ArrayList.class });
            List<StressHarian> list = (List<StressHarian>) xstream.fromXML(reader);
            riwayatData.setAll(list);
            refreshBarChart();
        } catch (Exception e) {
            System.out.println("Gagal memuat XML: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.show();
    }

    private void clearFields() {
        tfTest1.clear();
        tfTest2.clear();
        tfTest3.clear();
        tfTest4.clear();
        tfTest5.clear();
        datePicker.setValue(null);
    }
}
