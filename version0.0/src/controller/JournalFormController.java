package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import model.JournalDataManager;
import model.JournalEntry;

public class JournalFormController implements Initializable {
    @FXML
    private TextField titleField;
    @FXML
    private TextArea contentArea;
    @FXML
    private ComboBox<String> moodComboBox;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    private JournalDataManager dataManager;
    private JournalEntry currentEntry;
    private Mode mode;

    public enum Mode {
        ADD, EDIT
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupMoodComboBox();
        setupDatePicker();
        setupValidation();
    }

    private void setupMoodComboBox() {
        moodComboBox.getItems().addAll(
                "😊 Happy",
                "😢 Sad",
                "😴 Tired",
                "😤 Angry",
                "😰 Anxious",
                "😌 Calm",
                "🤔 Thoughtful",
                "😍 Excited",
                "😑 Neutral",
                "🤒 Sick",
                "😎 Confident",
                "😭 Overwhelmed");
        moodComboBox.getSelectionModel().selectFirst(); // Default mood
    }

    private void setupDatePicker() {
        datePicker.setValue(LocalDate.now());
    }

    private void setupValidation() {
        // Add listeners untuk real-time validation
        titleField.textProperty().addListener((observable, oldValue, newValue) -> validateForm());
        contentArea.textProperty().addListener((observable, oldValue, newValue) -> validateForm());
        moodComboBox.valueProperty().addListener((observable, oldValue, newValue) -> validateForm());
        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> validateForm());
    }

    private void validateForm() {
        boolean isValid = !titleField.getText().trim().isEmpty() &&
                !contentArea.getText().trim().isEmpty() &&
                moodComboBox.getValue() != null &&
                datePicker.getValue() != null;

        saveButton.setDisable(!isValid);
    }

    public void setDataManager(JournalDataManager dataManager) {
        this.dataManager = dataManager;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
        if (mode == Mode.EDIT) {
            saveButton.setText("Update");
        } else {
            saveButton.setText("Save");
        }
    }

    public void setJournalEntry(JournalEntry entry) {
        this.currentEntry = entry;
        if (entry != null) {
            titleField.setText(entry.getTitle());
            contentArea.setText(entry.getContent());
            moodComboBox.setValue(entry.getMood());
            datePicker.setValue(entry.getDate());
        }
    }

    @FXML
    private void handleSave(ActionEvent event) {
        if (!validateInput()) {
            return;
        }

        try {
            if (mode == Mode.ADD) {
                JournalEntry newEntry = new JournalEntry(
                        titleField.getText().trim(),
                        contentArea.getText().trim(),
                        moodComboBox.getValue(),
                        datePicker.getValue());
                dataManager.addJournalEntry(newEntry);
                showSuccessAlert("Journal entry added successfully!");
            } else if (mode == Mode.EDIT && currentEntry != null) {
                currentEntry.setTitle(titleField.getText().trim());
                currentEntry.setContent(contentArea.getText().trim());
                currentEntry.setMood(moodComboBox.getValue());
                currentEntry.setDate(datePicker.getValue());
                dataManager.updateJournalEntry(currentEntry);
                showSuccessAlert("Journal entry updated successfully!");
            }

            closeWindow();

        } catch (Exception e) {
            showErrorAlert("Error saving journal entry", e.getMessage());
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        closeWindow();
    }

    private boolean validateInput() {
        StringBuilder errors = new StringBuilder();

        if (titleField.getText() == null || titleField.getText().trim().isEmpty()) {
            errors.append("- Title is required\n");
        }

        if (contentArea.getText() == null || contentArea.getText().trim().isEmpty()) {
            errors.append("- Content is required\n");
        }

        if (moodComboBox.getValue() == null) {
            errors.append("- Mood selection is required\n");
        }

        if (datePicker.getValue() == null) {
            errors.append("- Date is required\n");
        } else if (datePicker.getValue().isAfter(LocalDate.now())) {
            errors.append("- Date cannot be in the future\n");
        }

        if (errors.length() > 0) {
            showErrorAlert("Validation Error", "Please fix the following errors:\n\n" + errors.toString());
            return false;
        }

        return true;
    }

    private void closeWindow() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method untuk focus pada title field saat form dibuka
    public void focusOnTitle() {
        titleField.requestFocus();
    }
}