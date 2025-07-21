package controller.homeDr;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Patient;
import model.PatientService;
import model.SessionManager;
import util.PatientXmlHandler;
import util.ArrayList;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class HomepageDrController implements Initializable {

    // Label
    @FXML
    private Label totalOnGoinPatient;
    @FXML
    private Label totalDonePatient;

    // History Table
    @FXML
    private TableView<Patient> tableHistoryMedical;
    @FXML
    private TableColumn<Patient, String> NamePatient;
    @FXML
    private TableColumn<Patient, LocalDate> dateConsult;
    @FXML
    private TableColumn<Patient, String> statusPatient;

    // Queue Table
    @FXML
    private TableView<Patient> tableQueuePatient;
    @FXML
    private TableColumn<Patient, String> NamePatientQueue;
    @FXML
    private TableColumn<Patient, LocalDate> dateConsultQueue;

    // Buttons
    @FXML
    private Button addFromTablePatient;
    @FXML
    private Button btnDoneQueue;

    // Queue List
    private ArrayList<Patient> queuePatients;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupColumns();

        // Load data from XML
        String username = SessionManager.getCurrentUser().getUsername();
        PatientService.loadPatientsFromXml(username); // active
        PatientService.loadPastPatientsFromXml(username); // past
        queuePatients = PatientXmlHandler.loadQueuePatients(username); // queue

        // Update view
        refreshQueueTable();
        refreshHistoryTable();
        updateSummary();

        // Button events
        addFromTablePatient.setOnAction(e -> addSelectedToQueue());
        btnDoneQueue.setOnAction(e -> markQueuePatientAsDone());
    }

    private void setupColumns() {
        // History
        NamePatient.setCellValueFactory(new PropertyValueFactory<>("name"));
        dateConsult.setCellValueFactory(new PropertyValueFactory<>("dateConsultation"));
        statusPatient.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Queue
        NamePatientQueue.setCellValueFactory(new PropertyValueFactory<>("name"));
        dateConsultQueue.setCellValueFactory(new PropertyValueFactory<>("dateConsultation"));
    }

    private void refreshHistoryTable() {
        tableHistoryMedical.getItems().clear();
        for (Patient p : PatientService.getActivePatients()) {
            tableHistoryMedical.getItems().add(p);
        }
    }

    private void refreshQueueTable() {
        tableQueuePatient.getItems().clear();
        for (Patient p : queuePatients) {
            tableQueuePatient.getItems().add(p);
        }
    }

    private void updateSummary() {
        int ongoing = PatientService.getActivePatients().size();
        int done = PatientService.getPastPatients().size();

        totalOnGoinPatient.setText(String.valueOf(ongoing));
        totalDonePatient.setText(String.valueOf(done));
    }

    private void addSelectedToQueue() {
        Patient selected = tableHistoryMedical.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a patient from the history to add to queue.",
                    Alert.AlertType.WARNING);
            return;
        }

        if (!queuePatients.contains(selected)) {
            queuePatients.add(selected);
            saveQueue();
            refreshQueueTable();
            showAlert("Success", "Patient added to queue.", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Already in Queue", "Patient is already in the queue.", Alert.AlertType.INFORMATION);
        }
    }

    private void markQueuePatientAsDone() {
        Patient selected = tableQueuePatient.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a patient in the queue.", Alert.AlertType.WARNING);
            return;
        }

        selected.setStatus("Completed");
        queuePatients.remove(selected);
        PatientService.moveToPast(selected.getPatientId());

        String username = SessionManager.getCurrentUser().getUsername();
        PatientXmlHandler.saveToXml(PatientService.getActivePatients(), username);
        PatientXmlHandler.savePastPatients(PatientService.getPastPatients(), username);
        saveQueue();

        refreshQueueTable();
        refreshHistoryTable();
        updateSummary();

        showAlert("Done", "Patient marked as done and archived.", Alert.AlertType.INFORMATION);
    }

    private void saveQueue() {
        String username = SessionManager.getCurrentUser().getUsername();
        PatientXmlHandler.saveQueuePatients(queuePatients, username);
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
