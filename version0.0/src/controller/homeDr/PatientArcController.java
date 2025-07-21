package controller.homeDr;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Patient;
import model.PatientService;
import model.SessionManager;
import util.PatientXmlHandler;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class PatientArcController implements Initializable {

    // Active Patients Table
    @FXML
    private TableView<Patient> tablePatientInformation;
    @FXML
    private TableColumn<Patient, String> colNamePatient;
    @FXML
    private TableColumn<Patient, LocalDate> colDateOfBirth;
    @FXML
    private TableColumn<Patient, String> colPattientIssue;
    @FXML
    private TableColumn<Patient, LocalDate> colDateConsult;
    @FXML
    private TableColumn<Patient, String> colStatus;

    // Past Patients Table
    @FXML
    private TableView<Patient> tablePastPatient;
    @FXML
    private TableColumn<Patient, String> colNamePatientPast;
    @FXML
    private TableColumn<Patient, LocalDate> colDateOfBirthPast;
    @FXML
    private TableColumn<Patient, String> colPattientIssuePast;
    @FXML
    private TableColumn<Patient, LocalDate> colDateConsultPast;
    @FXML
    private TableColumn<Patient, String> colStatusPast;

    // Buttons
    @FXML
    private Button btnAddPatient;
    @FXML
    private Button btnEditPatientInfo;
    @FXML
    private Button btnClearPatient;
    @FXML
    private Button btnDeletePastPatients;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTableColumns();
        setupButtonEvents();

        String username = SessionManager.getCurrentUser().getUsername();

        PatientService.loadPatientsFromXml(username);
        PatientService.loadPastPatientsFromXml(username);

        refreshAllTables();
    }

    private void setupTableColumns() {
        // Active patients
        colNamePatient.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDateOfBirth.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        colPattientIssue.setCellValueFactory(new PropertyValueFactory<>("patientIssue"));
        colDateConsult.setCellValueFactory(new PropertyValueFactory<>("dateConsultation"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Past patients
        colNamePatientPast.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDateOfBirthPast.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        colPattientIssuePast.setCellValueFactory(new PropertyValueFactory<>("patientIssue"));
        colDateConsultPast.setCellValueFactory(new PropertyValueFactory<>("dateConsultation"));
        colStatusPast.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    public void refreshAllTables() {
        refreshActivePatientTable();
        refreshPastPatientTable();
    }

    private void refreshActivePatientTable() {
        tablePatientInformation.getItems().clear();
        for (Patient p : PatientService.getActivePatients()) {
            tablePatientInformation.getItems().add(p);
        }
    }

    private void refreshPastPatientTable() {
        tablePastPatient.getItems().clear();
        for (Patient p : PatientService.getPastPatients()) {
            tablePastPatient.getItems().add(p);
        }
    }

    private void setupButtonEvents() {
        btnAddPatient.setOnAction(e -> openPatientForm(null));
        btnEditPatientInfo.setOnAction(e -> editSelectedPatient());
        btnClearPatient.setOnAction(e -> movePatientToPast());
        btnDeletePastPatients.setOnAction(this::handleDeletePastPatients);
    }

    private void openPatientForm(Patient patient) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DashboardProf/addPatientForm.fxml"));
            Parent root = loader.load();

            PatientFormController controller = loader.getController();
            controller.setParentController(this);
            controller.setPatientForEdit(patient);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(patient == null ? "Add New Patient" : "Edit Patient");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();

            refreshAllTables();
        } catch (Exception e) {
            showAlert("Error", "Failed to open patient form: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void editSelectedPatient() {
        Patient selected = tablePatientInformation.getSelectionModel().getSelectedItem();
        if (selected != null) {
            openPatientForm(selected);
        } else {
            showAlert("Warning", "Please select a patient to edit.", Alert.AlertType.WARNING);
        }
    }

    private void movePatientToPast() {
        Patient selected = tablePatientInformation.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Warning", "Please select a patient to move.", Alert.AlertType.WARNING);
            return;
        }

        if (!"Completed".equalsIgnoreCase(selected.getStatus())) {
            showAlert("Warning", "Only patients with status 'Completed' can be archived.",
                    Alert.AlertType.WARNING);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Archive Patient");
        confirm.setHeaderText("Move this patient to past archive?");
        confirm.setContentText("This action cannot be undone.");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean success = PatientService.moveToPast(selected.getPatientId());
                if (success) {
                    String username = SessionManager.getCurrentUser().getUsername();
                    PatientXmlHandler.savePastPatients(PatientService.getPastPatients(), username);
                    refreshAllTables();
                    showAlert("Success", "Patient archived successfully.", Alert.AlertType.INFORMATION);
                } else {
                    showAlert("Error", "Failed to archive patient.", Alert.AlertType.ERROR);
                }
            }
        });
    }

    @FXML
    private void handleDeletePastPatients(ActionEvent event) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Past Patients");
        confirm.setHeaderText("This will delete all past patients.");
        confirm.setContentText("Are you sure? This action is permanent.");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                PatientService.getPastPatients().clear();
                String username = SessionManager.getCurrentUser().getUsername();
                PatientXmlHandler.clearPastPatientsXml(username);
                refreshPastPatientTable();
                showAlert("Success", "All past patients deleted.", Alert.AlertType.INFORMATION);
            }
        });
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void printPatientStats() {
        System.out.println("Active Patients: " + PatientService.getActivePatients().size());
        System.out.println("Past Patients: " + PatientService.getPastPatients().size());
        System.out.println("Total Patients: " + PatientService.getTotalCount());
    }
}
