package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.MusicXMLHandler;

import java.util.ArrayList;
import java.util.List;

public class songService {
    private static ObservableList<Song> songs = FXCollections.observableArrayList();

    public static void loadSongs(String username) {
        List<Song> list = MusicXMLHandler.loadSongs(username);
        songs.setAll(list != null ? list : new ArrayList<>());
    }

    public static ObservableList<Song> getSongs() {
        return songs;
    }
}
