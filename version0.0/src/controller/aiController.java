package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import model.SessionManager;
import model.UserData;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import org.json.JSONObject;
import org.json.JSONArray;

import util.GeminiService;
import util.GroqService;
import util.MarkdownFormatter;

public class aiController implements Initializable {

    // @FXML
    // private TextArea chatArea;

    private VBox welcomeBox; // <- simpan sebagai field

    @FXML
    private VBox chatContainer;

    @FXML
    private ScrollPane chatScrollPane;

    @FXML
    private TextField inputField;

    // @Override
    // public void initialize(URL location, ResourceBundle resources) {
    // chatArea.setText("Hallo Aku ZenBot Ai! Asisten mental mu ^_^\n");
    // }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // addChatBubble("Hallo Aku ZenBot Ai! Asisten mental mu ^_^", false);
        showWelcomeMessage();
    }

    // ini grok service first line
    // =====================================================================================
    // @FXML
    // private void handleSendAction() {
    // String userInput = inputField.getText();
    // if (userInput == null || userInput.trim().isEmpty())
    // return;

    // addChatBubble(userInput, true); // Tampilkan bubble dari user
    // inputField.clear();

    // // Tampilkan indikator mengetik
    // HBox typingWrapper = addTypingIndicatorBubble();

    // // Hapus welcomeBox jika masih ada
    // if (welcomeBox != null && chatContainer.getChildren().contains(welcomeBox)) {
    // chatContainer.getChildren().remove(welcomeBox);
    // welcomeBox = null;
    // }

    // new Thread(() -> {
    // String botReply;

    // if (userInput.trim().equalsIgnoreCase("halo")) {
    // botReply = "Hello there, I'm ZenAI, your personal assistant 😊, what can I
    // help you with today?";
    // } else if (isBahasaIndonesia(userInput)) {
    // botReply = "Sorry, I only understand English. Please ask your question in
    // English.";
    // } else {
    // String rawResponse = GroqService.askGroq(userInput);
    // botReply = GroqService.cleanMarkdown(rawResponse);
    // }

    // String finalReply = botReply;

    // // Tampilkan balasan di UI JavaFX thread
    // Platform.runLater(() -> {
    // chatContainer.getChildren().remove(typingWrapper); // hapus indikator
    // Label botBubble = addChatBubble("", false); // bubble kosong
    // simulateTypingEffect(finalReply, botBubble, 10); // efek ketik
    // });
    // }).start();
    // }

    // ini grok service last line
    // =====================================================================================

    // ini Gemini service Frist line
    // =====================================================================================
    @FXML
    private void handleSendAction() {
        String userInput = inputField.getText();
        if (userInput == null || userInput.trim().isEmpty())
            return;

        addChatBubble(userInput, true); // Tampilkan bubble dari user
        inputField.clear();

        // Tampilkan indikator mengetik
        HBox typingWrapper = addTypingIndicatorBubble();

        // Hapus welcome box jika masih ada
        if (welcomeBox != null && chatContainer.getChildren().contains(welcomeBox)) {
            chatContainer.getChildren().remove(welcomeBox);
            welcomeBox = null;
        }

        new Thread(() -> {
            String botReply;

            if (userInput.trim().equalsIgnoreCase("halo")) {
                botReply = "Hello there, I'm ZenAI, your personal assistant 😊, what can I help you with today?";
            } else {
                String rawResponse = GeminiService.generateContent(userInput);
                String rawReply = extractGeminiReply(rawResponse);
                botReply = MarkdownFormatter.clean(rawReply);
            }

            String finalReply = botReply;

            Platform.runLater(() -> {
                chatContainer.getChildren().remove(typingWrapper); // Hapus indikator
                Label botBubble = addChatBubble("", false); // Bubble kosong dahulu
                simulateTypingEffect(finalReply, botBubble, 10); // Efek ketik
            });
        }).start();
    }

    // ini Gemini service Lastt line
    // =====================================================================================
    private Label addChatBubble(String message, boolean isUser) {
        Label bubble = new Label(message);
        bubble.setWrapText(true);
        bubble.setMaxWidth(600);
        bubble.getStyleClass().add(isUser ? "user-bubble" : "bot-bubble");

        HBox bubbleWrapper = new HBox(bubble);
        bubbleWrapper.setAlignment(isUser ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        chatContainer.getChildren().add(bubbleWrapper);

        Platform.runLater(() -> chatScrollPane.setVvalue(1.0));
        return bubble;
    }

    private HBox addTypingIndicatorBubble() {
        HBox typingDots = createTypingIndicator();

        // Bungkus animasi dalam bubble bergaya bot
        HBox bubbleWrapper = new HBox();
        bubbleWrapper.setAlignment(Pos.CENTER_LEFT);
        bubbleWrapper.setPadding(new Insets(5));
        bubbleWrapper.getStyleClass().add("bot-bubble");

        // Tambahkan indikator ke dalam bubble
        bubbleWrapper.getChildren().add(typingDots);

        HBox outerWrapper = new HBox(bubbleWrapper);
        outerWrapper.setAlignment(Pos.CENTER_LEFT);

        chatContainer.getChildren().add(outerWrapper);
        Platform.runLater(() -> chatScrollPane.setVvalue(1.0));

        return outerWrapper;
    }

    // @FXML
    // private void handleClearAction() {
    // chatContainer.getChildren().clear();
    // addChatBubble("Hallo Aku ZenBot Ai! Asisten mental mu ^_^", false);
    // }

    // handel typing bot
    private void simulateTypingEffect(String message, Label label, int typingDelayMillis) {
        new Thread(() -> {
            StringBuilder typedText = new StringBuilder();
            for (char c : message.toCharArray()) {
                typedText.append(c);
                String current = typedText.toString();

                Platform.runLater(() -> label.setText(current));

                try {
                    Thread.sleep(typingDelayMillis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // @FXML
    // private void handleClearAction() {
    // chatArea.clear();
    // chatArea.setText("Hallo Aku ZenBot Ai! Asisten mental mu ^_^\n");
    // chatArea.setText("\n Untuk membuat Zenbot Aktif Anda harus terhubung pada
    // jaringan \n");

    // }
    private void showWelcomeMessage() {
        String namaUser = "User"; // default
        UserData user = SessionManager.getCurrentUser();
        if (user != null && user.getNama() != null) {
            namaUser = user.getNama();
        }
        Label helloLabel = new Label("Hello " + namaUser);

        helloLabel.setStyle(
                "-fx-font-size: 32px; -fx-text-fill: #015C55; -fx-font-weight: bold;");

        Label helpLabel = new Label("How can I help you today?");
        helpLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: #888;");

        welcomeBox = new VBox(5, helloLabel, helpLabel);
        welcomeBox.setAlignment(Pos.CENTER);
        welcomeBox.setPadding(new Insets(20));
        welcomeBox.setId("welcomeBox");

        chatContainer.getChildren().add(welcomeBox);
    }

    private HBox createTypingIndicator() {
        Circle dot1 = new Circle(5);
        Circle dot2 = new Circle(5);
        Circle dot3 = new Circle(5);

        dot1.setStyle("-fx-fill: gray;");
        dot2.setStyle("-fx-fill: gray;");
        dot3.setStyle("-fx-fill: gray;");

        HBox dots = new HBox(5, dot1, dot2, dot3);
        dots.setPadding(new Insets(5));
        dots.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        TranslateTransition tt1 = createBounce(dot1, 0);
        TranslateTransition tt2 = createBounce(dot2, 150);
        TranslateTransition tt3 = createBounce(dot3, 300);

        tt1.play();
        tt2.play();
        tt3.play();

        return dots;
    }

    private TranslateTransition createBounce(Circle circle, int delayMillis) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(600), circle);
        tt.setFromY(0);
        tt.setToY(-10);
        tt.setCycleCount(TranslateTransition.INDEFINITE);
        tt.setAutoReverse(true);
        tt.setDelay(Duration.millis(delayMillis));
        return tt;
    }

    // Config gemini awal
    // ==========================================================================
    private String extractGeminiReply(String rawJson) {
        try {
            JSONObject json = new JSONObject(rawJson);

            if (!json.has("candidates")) {
                // Coba ambil pesan error jika ada
                if (json.has("error")) {
                    JSONObject err = json.getJSONObject("error");
                    return "Gemini Error: " + err.optString("message", "Unknown error");
                }

                return "No candidates found in Gemini response.";
            }

            JSONArray candidates = json.getJSONArray("candidates");
            if (candidates.length() > 0) {
                JSONObject content = candidates.getJSONObject(0).getJSONObject("content");
                JSONArray parts = content.getJSONArray("parts");
                if (parts.length() > 0) {
                    return parts.getJSONObject(0).getString("text");
                }
            }

            return "No valid reply found in Gemini response.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Sorry, something went wrong parsing the response.";
        }
    }

    // Config gemini akhir
    // ==========================================================================

    private boolean isBahasaIndonesia(String input) {
        String lower = input.toLowerCase();

        // Kata-kata umum dalam Bahasa Indonesia
        String[] keywords = {
                "halo", "apa", "bagaimana", "kamu", "saya", "terima kasih", "tolong", "kenapa", "makan", "minum",
                "selamat"
        };

        for (String keyword : keywords) {
            if (lower.contains(keyword)) {
                return true;
            }
        }

        return false;
    }

}
