package controller.homepageZen;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import model.SessionManager;
import model.Song;
import model.songService;
import util.MusicXMLHandler;

public class MusicController implements Initializable {

    @FXML
    private Text labelMusic;

    @FXML
    private Button btnPreviousMusic, btnDeleteMusic, btnPlayMusic, btnUpdateMusic, btnAddMusic;

    @FXML
    private TableView<Song> table;

    @FXML
    private TableColumn<Song, String> titleColumn;

    @FXML
    private ImageView btnNextMusic;

    private ObservableList<Song> songList;
    private MediaPlayer mediaPlayer;
    private String username;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        username = SessionManager.getCurrentUser().getUsername();

        // ✅ Ambil langsung dari SongService
        songList = songService.getSongs();
        titleColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));
        table.setItems(songList);

        System.out.println("✅ Lagu dimuat: " + songList.size());
    }

    @FXML
    void addSong(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP3 Files", "*.mp3"));
        File file = fc.showOpenDialog(null);

        if (file != null) {
            String path = file.toURI().toString();
            String title = file.getName();
            Song song = new Song(title, path);
            songList.add(song);
            MusicXMLHandler.saveSongs(songList, username); // ✅ Gunakan list yang sudah dimuat

            showAlert("Lagu Ditambahkan", "Lagu \"" + title + "\" berhasil ditambahkan!");
        }
    }

    @FXML
    void deleteSong(ActionEvent event) {
        Song selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            songList.remove(selected);
            MusicXMLHandler.saveSongs(songList, username);
        }
    }

    @FXML
    void updateSong(ActionEvent event) {
        Song selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP3 Files", "*.mp3"));
            File file = fc.showOpenDialog(null);
            if (file != null) {
                selected.setTitle(file.getName());
                selected.setPath(file.toURI().toString());
                table.refresh();
                MusicXMLHandler.saveSongs(songList, username);
            }
        } else {
            System.out.println("⚠️ Pilih lagu yang ingin di-update.");
        }
    }

    @FXML
    void playSong(ActionEvent event) {
        Song selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            util.AudioManager.play(selected); // ✅ Ganti ke AudioManager
            labelMusic.setText("Playing: " + selected.getTitle());
        }
    }

    @FXML
    void stopSong(ActionEvent event) {
        util.AudioManager.stop(); // ✅ Ganti ke AudioManager
        labelMusic.setText("Stopped");
    }

    @FXML
    void nextSong(ActionEvent event) {
        int index = table.getSelectionModel().getSelectedIndex();
        if (index < songList.size() - 1) {
            table.getSelectionModel().select(index + 1);
            playSong(null);
        }
    }

    @FXML
    void prevSong(ActionEvent event) {
        int index = table.getSelectionModel().getSelectedIndex();
        if (index > 0) {
            table.getSelectionModel().select(index - 1);
            playSong(null);
        }
    }

    public void playSelected(Song song) {
        try {
            if (mediaPlayer != null)
                mediaPlayer.stop();

            Media media = new Media(song.getPath());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
            labelMusic.setText("Playing: " + song.getTitle());

        } catch (Exception e) {
            labelMusic.setText("Error playing song.");
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    public ObservableList<Song> getSongList() {
        return songList;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
