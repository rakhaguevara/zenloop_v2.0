package util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import model.Song;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MusicXMLHandler {

    private static final String DIR_PATH = "data_music/";
    private static final XStream xstream = new XStream(new StaxDriver());

    static {
        XStream.setupDefaultSecurity(xstream);
        xstream.allowTypesByWildcard(new String[] {
                "model.Song", "java.util.*"
        });

        xstream.alias("song", Song.class);
        xstream.alias("songs", List.class);
    }

    /**
     * Memuat lagu milik user berdasarkan username.
     */
    public static List<Song> loadSongs(String username) {
        File file = new File(DIR_PATH + username + "_songs.xml");
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return (List<Song>) xstream.fromXML(reader);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Menyimpan lagu milik user ke file pribadi berdasarkan username.
     */
    public static void saveSongs(List<Song> songs, String username) {
        try {
            File dir = new File(DIR_PATH);
            if (!dir.exists())
                dir.mkdirs();

            File file = new File(DIR_PATH + username + "_songs.xml");

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                xstream.toXML(new ArrayList<>(songs), writer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
