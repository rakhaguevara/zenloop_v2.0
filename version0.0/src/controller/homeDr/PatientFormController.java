package controller.homeDr;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import model.*;
import util.PatientXmlHandler;

public class PatientFormController implements Initializable {

    @FXML
    private Button btnDeletePatient;
    @FXML
    private TextField titleFieldName;
    @FXML
    private DatePicker dateOfBirth;
    @FXML
    private ComboBox<String> patientStatusComBox;
    @FXML
    private TextArea patientIssue;
    @FXML
    private Button cancelButton;
    @FXML
    private Button savePatientButton;

    private Patient currentPatient; // Untuk mode edit
    private PatientArcController parentController; // Reference ke archive controller

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupStatusComboBox();
        setupValidation();
    }

    private void setupStatusComboBox() {
        patientStatusComBox.setItems(FXCollections.observableArrayList(
                "Active Treatment",
                "Follow-up",
                "Consultation",
                "Emergency",
                "Completed"));
    }

    private void setupValidation() {
        // Enable save button only when required fields are filled
        savePatientButton.disableProperty().bind(
                titleFieldName.textProperty().isEmpty()
                        .or(dateOfBirth.valueProperty().isNull())
                        .or(patientStatusComBox.valueProperty().isNull())
                        .or(patientIssue.textProperty().isEmpty()));
    }

    public void setParentController(PatientArcController parentController) {
        this.parentController = parentController;
    }

    public void setPatientForEdit(Patient patient) {
        this.currentPatient = patient;
        if (patient != null) {
            titleFieldName.setText(patient.getName());
            dateOfBirth.setValue(patient.getDateOfBirth());
            patientStatusComBox.setValue(patient.getStatus());
            patientIssue.setText(patient.getPatientIssue());
            btnDeletePatient.setVisible(true);
        } else {
            clearForm();
            btnDeletePatient.setVisible(false);
        }
    }

    @FXML
    private void handleSave(ActionEvent event) {
        try {
            String name = titleFieldName.getText().trim();
            LocalDate dob = dateOfBirth.getValue();
            String status = patientStatusComBox.getValue();
            String issue = patientIssue.getText().trim();

            PatientService.validatePatientData(name, dob, status, issue);

            String username = SessionManager.getCurrentUser().getUsername();

            if (currentPatient == null) {
                // Tambah pasien baru
                PatientService.addNewPatient(name, dob, status, issue, username);
                showAlert("Success", "Patient added successfully!", Alert.AlertType.INFORMATION);
            } else {
                // Edit pasien
                boolean updated = PatientService.updatePatient(
                        currentPatient.getPatientId(), name, dob, status, issue);
                if (updated) {
                    showAlert("Success", "Patient updated successfully!", Alert.AlertType.INFORMATION);
                } else {
                    showAlert("Error", "Failed to update patient!", Alert.AlertType.ERROR);
                    return;
                }
            }

            // ✅ Simpan ke file XML
            PatientXmlHandler.saveToXml(
                    new ArrayList<>(PatientService.getActivePatients()),
                    username);

            closeWindow();

        } catch (IllegalArgumentException e) {
            showAlert("Validation Error", e.getMessage(), Alert.AlertType.WARNING);
        } catch (Exception e) {
            showAlert("Error", "Failed to save patient: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        closeWindow();
    }

    @FXML
    private void handleDeletePatient(ActionEvent event) {
        if (currentPatient != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Delete Patient");
            confirmation.setHeaderText("Are you sure you want to delete this patient?");
            confirmation.setContentText("This action cannot be undone.");

            confirmation.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    boolean deleted = PatientService.deletePatient(currentPatient.getPatientId());
                    if (deleted) {
                        // ✅ Simpan ulang data ke XML sesuai dokter yang sedang login
                        String username = SessionManager.getCurrentUser().getUsername();
                        PatientXmlHandler.saveToXml(
                                new ArrayList<>(PatientService.getActivePatients()),
                                username);

                        showAlert("Success", "Patient deleted successfully!", Alert.AlertType.INFORMATION);
                        closeWindow();
                    } else {
                        showAlert("Error", "Failed to delete patient!", Alert.AlertType.ERROR);
                    }
                }
            });
        }
    }

    private void clearForm() {
        titleFieldName.clear();
        dateOfBirth.setValue(null);
        patientStatusComBox.setValue(null);
        patientIssue.clear();
    }

    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}