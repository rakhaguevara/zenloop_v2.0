package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.VideoData;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RelaxContentController implements Initializable {

    @FXML
    private ScrollPane videoScrollPane;

    @FXML
    private HBox videoContainer;

    private List<VideoData> videoDataList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupVideoData();
        loadVideoCards();
    }

    private void setupVideoData() {
        videoDataList = new ArrayList<>();

        // Contoh data video - Anda bisa mengganti dengan data dari database atau API
        videoDataList.add(new VideoData(
                "Clip 1",
                "https://www.youtube.com/watch?v=of3_8r3F40Q",
                "/app/resource/yt1.png"));

        videoDataList.add(new VideoData(
                "Clip 2",
                "https://www.youtube.com/watch?v=rkZl2gsLUp4",
                "/app/resource/yt2.png"));

        videoDataList.add(new VideoData(
                "Clip 4",
                "https://www.youtube.com/watch?v=8TY8oJh05jQ",
                "/app/resource/yt3.png"));

        videoDataList.add(new VideoData(
                "Clip 4",
                "https://www.youtube.com/watch?v=QYzUkpIjn6s",
                "/app/resource/yt4.png"));
    }

    private void loadVideoCards() {
        if (videoContainer == null) {
            // Jika videoContainer belum di-inject, cari ScrollPane dan ambil content-nya
            findVideoContainer();
        }

        videoContainer.getChildren().clear();

        for (VideoData video : videoDataList) {
            VBox videoCard = createVideoCard(video);
            videoContainer.getChildren().add(videoCard);
        }
    }

    private void findVideoContainer() {
        // Mencari HBox container di dalam ScrollPane
        if (videoScrollPane != null && videoScrollPane.getContent() instanceof HBox) {
            videoContainer = (HBox) videoScrollPane.getContent();
        }
    }

    private VBox createVideoCard(VideoData videoData) {
        VBox cardContainer = new VBox();
        cardContainer.setPrefWidth(339);
        cardContainer.setPrefHeight(240);
        cardContainer.getStyleClass().add("card-content");
        // cardContainer.getStylesheets().add(getClass().getResource("/css/card-journal.css").toExternalForm());

        // Margin untuk card
        HBox.setMargin(cardContainer, new Insets(0, 14, 0, 0));

        // Container untuk thumbnail dan button
        javafx.scene.layout.AnchorPane imageContainer = new javafx.scene.layout.AnchorPane();
        imageContainer.setPrefHeight(200);
        imageContainer.setPrefWidth(339);

        // Thumbnail image
        ImageView thumbnail = new ImageView();
        thumbnail.setFitHeight(292);
        thumbnail.setFitWidth(339);
        thumbnail.setPreserveRatio(true);
        thumbnail.setPickOnBounds(true);

        try {
            Image image = new Image(getClass().getResourceAsStream(videoData.getThumbnailPath()));
            thumbnail.setImage(image);
        } catch (Exception e) {
            // Jika gambar tidak ditemukan, gunakan placeholder
            System.out.println("Thumbnail tidak ditemukan: " + videoData.getThumbnailPath());
        }

        // Watch Now button
        Button watchButton = new Button("Watch Now");
        watchButton.setPrefHeight(34);
        watchButton.setPrefWidth(92);
        watchButton.setLayoutX(225);
        watchButton.setLayoutY(175);
        watchButton.getStyleClass().add("button-add");
        watchButton.getStylesheets().add(getClass().getResource("/view/button.css").toExternalForm());
        watchButton.setFont(new Font(9));

        // Event handler untuk button
        watchButton.setOnAction(e -> openVideoPlayer(videoData));

        // Title text (opsional)
        Text titleText = new Text(videoData.getTitle());
        titleText.setLayoutX(10);
        titleText.setLayoutY(15);
        titleText.setFont(new Font(12));
        titleText.setWrappingWidth(300);

        // Tambahkan semua elemen ke container
        imageContainer.getChildren().addAll(thumbnail, watchButton, titleText);
        cardContainer.getChildren().add(imageContainer);

        return cardContainer;
    }

    private void openVideoPlayer(VideoData videoData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DashboardZen/preiewVideo.fxml"));
            Parent videoPlayerRoot = loader.load();

            // Ambil controller video player
            VideoPlayerController playerController = loader.getController();
            playerController.loadVideo(videoData);

            // Buat stage baru untuk video player
            Stage videoStage = new Stage();
            videoStage.setTitle("Playing: " + videoData.getTitle());
            videoStage.setScene(new Scene(videoPlayerRoot));
            videoStage.initModality(Modality.APPLICATION_MODAL);

            // Tambahkan handler saat window ditutup
            videoStage.setOnCloseRequest(e -> {
                try {
                    playerController.pauseVideo(); // pause videonya
                    playerController.cleanup(); // hapus video jika perlu
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            // Tampilkan video player
            videoStage.showAndWait();

        } catch (IOException e) {
            System.err.println("Error loading video player: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Method untuk refresh video list (jika diperlukan)
    public void refreshVideoList() {
        setupVideoData();
        loadVideoCards();
    }

    // Method untuk menambah video baru
    public void addVideo(VideoData videoData) {
        videoDataList.add(videoData);
        loadVideoCards();
    }

    // Method untuk menghapus video
    public void removeVideo(VideoData videoData) {
        videoDataList.remove(videoData);
        loadVideoCards();
    }
}