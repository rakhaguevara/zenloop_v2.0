package controller.homepageZen;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
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
        // Ambil user login aktif
        username = SessionManager.getCurrentUser().getUsername();

        // Inisialisasi kolom tabel
        titleColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));

        try {
            List<Song> loadedSongs = MusicXMLHandler.loadSongs(username);
            songList = FXCollections.observableArrayList(loadedSongs != null ? loadedSongs : new ArrayList<>());
            table.setItems(songList);
            System.out.println("✅ Lagu dimuat: " + songList.size());
        } catch (Exception e) {
            e.printStackTrace();
            songList = FXCollections.observableArrayList();
            table.setItems(songList);
        }
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
            MusicXMLHandler.saveSongs(new ArrayList<>(songList), username);

            // 🎉 Tampilkan notifikasi
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Lagu Ditambahkan");
            alert.setHeaderText(null);
            alert.setContentText("Lagu \"" + title + "\" berhasil ditambahkan!");
            alert.showAndWait();
        }
    }

    @FXML
    void deleteSong(ActionEvent event) {
        Song selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            songList.remove(selected);
            MusicXMLHandler.saveSongs(new ArrayList<>(songList), username);
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
                MusicXMLHandler.saveSongs(new ArrayList<>(songList), username);
            }
        } else {
            System.out.println("⚠️ Pilih lagu yang ingin di-update.");
        }
    }

    @FXML
    void playSong(ActionEvent event) {
        Song selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }

            try {
                Media media = new Media(selected.getPath());
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.play();
                labelMusic.setText("Playing: " + selected.getTitle());
            } catch (Exception e) {
                System.out.println("❌ Gagal memutar lagu: " + e.getMessage());
                labelMusic.setText("Error playing song.");
            }
        }
    }

    @FXML
    void stopSong(ActionEvent event) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            labelMusic.setText("Stopped");
        }
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
}
