package controller;

import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

public class StatisticController {

    @FXML
    private BarChart<String, Number> barChartStress;

    @FXML
    private CategoryAxis hariAxis;

    @FXML
    private NumberAxis nilaiAxis;

    @FXML
    private Button btnDeleteData;

    @FXML
    private Button btnEditStatistic;

    @FXML
    private Button btnSaveStatistic;

    @FXML
    private AnchorPane centerPane;

    @FXML
    private TableView<?> tabelStressStatistic;

    @FXML
    private TableColumn<?, ?> colTanggal;

    @FXML
    private TableColumn<?, ?> colRataRata;

    @FXML
    private TableColumn<?, ?> colKeterangan;

    @FXML
    private TextField tfTest1;

    @FXML
    private TextField tfTest2;

    @FXML
    private TextField tfTest3;

    @FXML
    private TextField tfTest4;

    @FXML
    private TextField tfTest5;

    private XYChart.Series<String, Number> seriesData = new XYChart.Series<>();
    private int indexHari = 0;
    private final String[] hariMinggu = { "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu" };
}
