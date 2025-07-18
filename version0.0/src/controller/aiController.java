package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.application.Platform;
import org.json.JSONObject;
import org.json.JSONArray;
import util.GroqService;

public class aiController implements Initializable {

    @FXML
    private TextArea chatArea;

    @FXML
    private TextField inputField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chatArea.setText("Hallo Aku ZenBot Ai! Asisten mental mu ^_^\n");
    }

    @FXML
    private void handleSendAction() {
        String userInput = inputField.getText();
        if (userInput == null || userInput.trim().isEmpty())
            return;

        chatArea.appendText("You : " + userInput + "\n");
        inputField.clear();

        // Tampilkan placeholder mengetik
        chatArea.appendText("Bot: sedang menulis...\n");

        // Jalankan request di thread terpisah
        new Thread(() -> {
            String botReply = GroqService.cleanMarkdown(GroqService.askGroq(userInput));

            // Update UI di thread JavaFX
            Platform.runLater(() -> {
                String currentText = chatArea.getText();
                int typingIndex = currentText.lastIndexOf("Bot: sedang menulis...");
                if (typingIndex != -1) {
                    chatArea.replaceText(typingIndex, currentText.length(), "Bot: " + botReply + "\n");
                } else {
                    chatArea.appendText("Bot: " + botReply + "\n");
                }
            });
        }).start();
    }

    @FXML
    private void handleClearAction() {
        chatArea.clear();
        chatArea.setText("Hallo Aku ZenBot Ai! Asisten mental mu ^_^\n");
        chatArea.setText("\n Untuk membuat Zenbot Aktif Anda harus terhubung pada jaringan \n");

    }

}
