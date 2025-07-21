package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import model.VideoData;

import java.net.URL;
import java.util.ResourceBundle;

public class VideoPlayerController implements Initializable {

    @FXML
    private WebView videoPlayContent;

    private WebEngine webEngine;
    private VideoData currentVideo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        webEngine = videoPlayContent.getEngine();

        // Mengaktifkan JavaScript (diperlukan untuk YouTube)
        webEngine.setJavaScriptEnabled(true);

        // Optional: Disable context menu
        videoPlayContent.setContextMenuEnabled(false);

        // Optional: Set user agent untuk kompatibilitas yang lebih baik
        webEngine.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");
    }

    public void loadVideo(VideoData videoData) {
        this.currentVideo = videoData;
        String embedUrl = videoData.getEmbedUrl();

        // HTML template untuk embed YouTube video
        String htmlContent = generateVideoHTML(embedUrl, videoData.getTitle());

        // Load HTML content ke WebView
        webEngine.loadContent(htmlContent);
    }

    private String generateVideoHTML(String embedUrl, String title) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <title>" + title + "</title>" +
                "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "    <style>" +
                "        html, body {" +
                "            margin: 0;" +
                "            padding: 0;" +
                "            width: 100%;" +
                "            height: 100%;" +
                "            background-color: #000;" +
                "            overflow: hidden;" +
                "        }" +
                "        iframe {" +
                "            position: absolute;" +
                "            top: 0;" +
                "            left: 0;" +
                "            width: 100%;" +
                "            height: 100%;" +
                "            border: none;" +
                "        }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <iframe src='" + embedUrl + "?rel=0&modestbranding=1'" +
                "        allow='encrypted-media' allowfullscreen>" +
                "    </iframe>" +
                "</body>" +
                "</html>";
    }

    // Method untuk mendapatkan video yang sedang dimainkan
    public VideoData getCurrentVideo() {
        return currentVideo;
    }

    // Method untuk load video baru tanpa membuka window baru
    public void switchVideo(VideoData newVideo) {
        loadVideo(newVideo);
    }

    public void cleanup() {
        try {
            // Stop video dan kosongkan WebView
            pauseVideo();
            webEngine.load(null); // Membersihkan iframe dari memori
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method untuk pause video
    public void pauseVideo() {
        webEngine.executeScript(
                "document.querySelector('iframe').contentWindow.postMessage('{\"event\":\"command\",\"func\":\"pauseVideo\",\"args\":\"\"}', '*');");
    }

}