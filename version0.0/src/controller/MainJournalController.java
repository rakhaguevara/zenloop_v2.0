package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Optional;
import model.*;

public class MainJournalController implements Initializable {
    @FXML
    private Button btnAddJournal;
    @FXML
    private Button btnDeleteJournal;
    @FXML
    private Button btnEditJournal;
    @FXML
    private TextArea contentTextArea;
    @FXML
    private VBox journalCardsContainer;

    private JournalDataManager dataManager;
    private JournalEntry selectedEntry;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dataManager = new JournalDataManager();
        setupComponents();
        refreshJournalList();
    }

    private void setupComponents() {
        // Setup text area untuk read-only mode
        if (contentTextArea != null) {
            contentTextArea.setEditable(false);
            contentTextArea.setWrapText(true);
            contentTextArea.setText("Select a journal entry to view its content here...");
        }

        // Setup initial button states
        updateButtonStates();
    }

    private void updateButtonStates() {
        boolean hasSelection = selectedEntry != null;
        if (btnEditJournal != null) {
            btnEditJournal.setDisable(!hasSelection);
        }
        if (btnDeleteJournal != null) {
            btnDeleteJournal.setDisable(!hasSelection);
        }
    }

    private void refreshJournalList() {
        if (journalCardsContainer == null) {
            // Jika belum ada container, cari VBox di ScrollPane
            return;
        }

        journalCardsContainer.getChildren().clear();

        for (JournalEntry entry : dataManager.getJournalEntries()) {
            HBox journalCard = createJournalCard(entry);
            journalCardsContainer.getChildren().add(journalCard);
        }
    }

    private HBox createJournalCard(JournalEntry entry) {
        HBox card = new HBox();
        card.getStyleClass().add("journal-card");
        card.setPrefHeight(133.0);
        card.setPrefWidth(339.0);
        VBox.setMargin(card, new Insets(8, 16, 0, 16));

        VBox cardContent = new VBox();
        cardContent.setPrefHeight(133.0);
        cardContent.setPrefWidth(337.0);

        // Title label
        Label titleLabel = new Label(entry.getTitle());
        titleLabel.getStyleClass().add("card-header");
        titleLabel.setFont(Font.font("Bold", 18.0));
        titleLabel.setPrefHeight(17.0);
        titleLabel.setPrefWidth(295.0);
        VBox.setMargin(titleLabel, new Insets(24, 0, 0, 24));

        // Content preview label
        Label contentLabel = new Label(entry.getPreview());
        contentLabel.getStyleClass().add("card-header");
        contentLabel.setPrefHeight(17.0);
        contentLabel.setPrefWidth(292.0);
        VBox.setMargin(contentLabel, new Insets(4, 0, 0, 24));

        // Delete button (akan dipindah ke posisi yang benar)
        // Button deleteBtn = new Button("Delete");

        // deleteBtn.setPrefWidth( .0);
        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/app/resource/deleteIcn.png")));
        icon.setFitWidth(18);
        icon.setFitHeight(18);
        Button deleteBtn = new Button("", icon);
        deleteBtn.getStyleClass().add("delete-btn");
        VBox.setMargin(deleteBtn, new Insets(16, 0, 0, 280));
        deleteBtn.setPrefHeight(33.0);

        // Set action untuk delete button
        deleteBtn.setOnAction(e -> handleDeleteJournal(entry));

        cardContent.getChildren().addAll(titleLabel, contentLabel, deleteBtn);
        card.getChildren().add(cardContent);

        // Add click handler untuk seleksi card
        card.setOnMouseClicked(e -> {
            selectJournalEntry(entry);
        });

        return card;
    }

    private void selectJournalEntry(JournalEntry entry) {
        selectedEntry = entry;
        displayJournalContent(entry);
        updateButtonStates();

        // Update visual selection (optional - bisa ditambahkan CSS class)
        refreshJournalList(); // Refresh untuk update visual selection
    }

    private void displayJournalContent(JournalEntry entry) {
        if (contentTextArea != null && entry != null) {
            StringBuilder content = new StringBuilder();
            content.append("Title: ").append(entry.getTitle()).append("\n\n");
            content.append("Date: ").append(entry.getFormattedDate()).append("\n");
            content.append("Mood: ").append(entry.getMood()).append("\n\n");
            content.append("Content:\n");
            content.append(entry.getContent());

            contentTextArea.setText(content.toString());
        }
    }

    @FXML
    private void handleAddJournal(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/JournalFormFXML.fxml"));
            Parent root = loader.load();

            JournalFormController formController = loader.getController();
            formController.setDataManager(dataManager);
            formController.setMode(JournalFormController.Mode.ADD);

            // Scene scene = new Scene(root);
            // scene.getStylesheets().add(getClass().getResource("/view/card-journal.css").toExternalForm());

            Stage stage = new Stage();
            stage.setTitle("Add New Journal Entry");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            refreshJournalList();

        } catch (IOException e) {
            showErrorAlert("Error opening add journal form", e.getMessage());
        }
    }

    @FXML
    private void handleEditJournal(ActionEvent event) {
        if (selectedEntry == null) {
            showWarningAlert("No Selection", "Please select a journal entry to edit.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/JournalFormFXML.fxml"));
            Parent root = loader.load();

            JournalFormController formController = loader.getController();
            formController.setDataManager(dataManager);
            formController.setMode(JournalFormController.Mode.EDIT);
            formController.setJournalEntry(selectedEntry);

            Stage stage = new Stage();
            stage.setTitle("Edit Journal Entry");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            refreshJournalList();
            // Refresh content display jika entry yang sedang dipilih diedit
            if (selectedEntry != null) {
                displayJournalContent(selectedEntry);
            }

        } catch (IOException e) {
            showErrorAlert("Error opening edit journal form", e.getMessage());
        }
    }

    @FXML
    private void handleDeleteJournal(ActionEvent event) {
        if (selectedEntry == null) {
            showWarningAlert("No Selection", "Please select a journal entry to delete.");
            return;
        }

        performDelete(selectedEntry);
    }

    // Method overload untuk delete dari card button
    private void handleDeleteJournal(JournalEntry entry) {
        performDelete(entry);
    }

    private void performDelete(JournalEntry entry) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Journal Entry");
        alert.setHeaderText("Are you sure you want to delete this journal entry?");
        alert.setContentText("Title: " + entry.getTitle() + "\nThis action cannot be undone.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            dataManager.deleteJournalEntry(entry);

            // Clear selection jika entry yang dihapus sedang dipilih
            if (selectedEntry == entry) {
                selectedEntry = null;
                if (contentTextArea != null) {
                    contentTextArea.setText("Select a journal entry to view its content here...");
                }
            }

            refreshJournalList();
            updateButtonStates();
        }
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showWarningAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method untuk set container jika perlu diakses dari luar
    public void setJournalCardsContainer(VBox container) {
        this.journalCardsContainer = container;
    }

    // Method untuk set text area jika perlu diakses dari luar
    public void setContentTextArea(TextArea textArea) {
        this.contentTextArea = textArea;
    }
}