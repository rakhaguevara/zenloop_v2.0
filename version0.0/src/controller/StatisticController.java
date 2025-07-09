package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class StatisticController {

    @FXML
    private BarChart<String, Number> barChartStress;

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
    private TextField tfTest1;

    @FXML
    private TextField tfTest2;

    @FXML
    private TextField tfTest3;

    @FXML
    private TextField tfTest4;

    @FXML
    private TextField tfTest5;

    @FXML
    void btnHandleStatistic(ActionEvent event) {

    }

    @FXML
    void handleDeleteData(ActionEvent event) {

    }

    @FXML
    void handleSaveStatistic(ActionEvent event) {

    }

}
