// util/AudioManager.java
package util;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import model.Song;

public class AudioManager {
    private static MediaPlayer currentPlayer = null;

    public static void play(Song song) {
        stop(); // stop yang lama
        try {
            Media media = new Media(song.getPath());
            currentPlayer = new MediaPlayer(media);
            currentPlayer.play();
        } catch (Exception e) {
            System.out.println("❌ Error play: " + e.getMessage());
        }
    }

    public static void stop() {
        if (currentPlayer != null) {
            currentPlayer.stop();
            currentPlayer.dispose();
            currentPlayer = null;
        }
    }

    public static boolean isPlaying() {
        return currentPlayer != null;
    }
}
