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
import java.util.ArrayList;
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
        bindTableToModel(); // Langsung binding ke model
        setupButtonEvents();
        // ✅ Ambil username dari session
        String username = SessionManager.getCurrentUser().getUsername();

        // ✅ Load data pasien aktif dan arsip sesuai dokter yang login
        PatientService.loadPatientsFromXml(username);
        PatientService.loadPastPatientsFromXml(username);

    }

    private void setupTableColumns() {
        // Active patients table columns
        colNamePatient.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDateOfBirth.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        colPattientIssue.setCellValueFactory(new PropertyValueFactory<>("patientIssue"));
        colDateConsult.setCellValueFactory(new PropertyValueFactory<>("dateConsultation"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Past patients table columns
        colNamePatientPast.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDateOfBirthPast.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        colPattientIssuePast.setCellValueFactory(new PropertyValueFactory<>("patientIssue"));
        colDateConsultPast.setCellValueFactory(new PropertyValueFactory<>("dateConsultation"));
        colStatusPast.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void bindTableToModel() {
        // Direct binding ke ObservableList di model - AUTO SYNC!
        tablePatientInformation.setItems(PatientService.getActivePatients());
        tablePastPatient.setItems(PatientService.getPastPatients());

        // Tidak perlu manual refresh lagi karena ObservableList otomatis sync
    }

    private void setupButtonEvents() {
        btnAddPatient.setOnAction(event -> openPatientForm(null));
        btnEditPatientInfo.setOnAction(event -> editSelectedPatient());
        btnClearPatient.setOnAction(event -> movePatientToPast());
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

        } catch (Exception e) {
            showAlert("Error", "Failed to open patient form: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void editSelectedPatient() {
        Patient selectedPatient = tablePatientInformation.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            openPatientForm(selectedPatient);
        } else {
            showAlert("Warning", "Please select a patient to edit.", Alert.AlertType.WARNING);
        }
    }

    private void movePatientToPast() {
        Patient selectedPatient = tablePatientInformation.getSelectionModel().getSelectedItem();
        if (selectedPatient != null) {
            if (!"Completed".equalsIgnoreCase(selectedPatient.getStatus())) {
                showAlert("Warning", "Only patients with status 'Completed' can be archived.",
                        Alert.AlertType.WARNING);
                return;
            }

            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Move to Past Patients");
            confirmation.setHeaderText("Move patient to past patients archive?");
            confirmation.setContentText("This will move the patient from active to past patients list.");

            confirmation.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    boolean moved = PatientService.moveToPast(selectedPatient.getPatientId());
                    if (moved) {
                        // ✅ Simpan ke XML folder khusus past patient
                        String username = SessionManager.getCurrentUser().getUsername();
                        PatientXmlHandler.savePastPatients(
                                new ArrayList<>(PatientService.getPastPatients()), username);

                        showAlert("Success", "Patient moved to past patients successfully!",
                                Alert.AlertType.INFORMATION);
                    } else {
                        showAlert("Error", "Failed to move patient!", Alert.AlertType.ERROR);
                    }
                }
            });
        } else {
            showAlert("Warning", "Please select a patient to move to past patients.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void handleDeletePastPatients(ActionEvent event) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Delete All Past Patients");
        confirmation.setHeaderText("Are you sure you want to delete all past patients?");
        confirmation.setContentText("This will remove all records from past patients and clear the XML file.");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Kosongkan ObservableList past patients
                PatientService.getPastPatients().clear();

                // Kosongkan file XML
                String username = SessionManager.getCurrentUser().getUsername();
                PatientXmlHandler.clearPastPatientsXml(username);

                showAlert("Success", "All past patients deleted successfully!", Alert.AlertType.INFORMATION);
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

    // Optional: Method untuk debugging atau monitoring
    public void printPatientStats() {
        System.out.println("Active Patients: " + PatientService.getActivePatients().size());
        System.out.println("Past Patients: " + PatientService.getPastPatients().size());
        System.out.println("Total Patients: " + PatientService.getTotalCount());
    }
}