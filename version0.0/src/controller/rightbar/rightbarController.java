package controller.rightbar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import util.MusicXMLHandler;
import model.Song;
import model.SessionManager;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import controller.HomeController;
import controller.sidebar.SidebarController;

public class rightbarController implements Initializable {

    @FXML
    private Button btnAddMusic, btnPauseRight, btnPlayMusicRight, btnPreviousMusic, btnToConsult;

    @FXML
    private ImageView btnNextMusic;

    @FXML
    private Label labelTitle;

    @FXML
    private Label labelArtist;

    @FXML
    private AnchorPane rightBarPane;

    @FXML
    private BorderPane mainContentPane;

    // private List<Song> songList = new ArrayList<>();
    // private ObservableList<Song> songList = FXCollections.observableArrayList();
    private ObservableList<Song> songList;

    private MediaPlayer mediaPlayer;
    private int currentSongIndex = 0;
    private String username;
    private controller.homepageZen.MusicController musicController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        username = SessionManager.getCurrentUser().getUsername();

        // Ambil list global dari SongService lebih dulu
        songList = model.songService.getSongs();

        // Event binding
        btnPlayMusicRight.setOnAction(e -> playSong());
        btnPauseRight.setOnAction(e -> stopSong());
        btnNextMusic.setOnMouseClicked(e -> nextSong());
        btnPreviousMusic.setOnAction(e -> prevSong());
        btnAddMusic.setOnAction(e -> addSong());
    }

    @FXML
    private void handleToConsult() {

        SidebarController sidebar = SidebarController.getInstance();

        if (sidebar != null) {
            sidebar.handleFindYourKonselorClick();
        }

        if (HomeController.getInstance() != null) {
            HomeController.getInstance().showFindYourKonselor();
        }

    }

    private void playSong() {
        if (songList.isEmpty())
            return;

        if (mediaPlayer != null)
            mediaPlayer.stop();

        try {
            Song song = songList.get(currentSongIndex);

            labelTitle.setText(song.getTitle());
            labelArtist.setText("Unknown Artist"); // atau song.getArtist() jika kamu punya field artist

            Media media = new Media(song.getPath());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
        } catch (Exception e) {
            showError("Gagal memutar lagu");
        }
    }

    private void stopSong() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    private void nextSong() {
        if (songList.isEmpty())
            return;
        currentSongIndex = (currentSongIndex + 1) % songList.size();
        playSong();
    }

    private void prevSong() {
        if (songList.isEmpty())
            return;
        currentSongIndex = (currentSongIndex - 1 + songList.size()) % songList.size();
        playSong();
    }

    private void addSong() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP3 Files", "*.mp3"));
        File file = fc.showOpenDialog(null);

        if (file != null) {
            String path = file.toURI().toString();
            String title = file.getName();
            Song song = new Song(title, path);

            // ✅ Tambahkan langsung ke list SongService
            songList.add(song);
            MusicXMLHandler.saveSongs(songList, username);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Lagu Ditambahkan");
            alert.setHeaderText(null);
            alert.setContentText("Lagu \"" + title + "\" berhasil ditambahkan!");
            alert.showAndWait();
        }
    }

    public void bindToMusicList(ObservableList<Song> externalList) {
        this.songList = externalList;
    }

    public ObservableList<Song> getSongList() {
        return songList;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setMusicController(controller.homepageZen.MusicController controller) {
        this.musicController = controller;
    }
}
