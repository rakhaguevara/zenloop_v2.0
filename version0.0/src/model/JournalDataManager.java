package model;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JournalDataManager {
    private static final String XML_FILE = "data/journal_archive/journal_entries.xml";
    private String username;

    private ObservableList<JournalEntry> journalEntries;
    private XStream xstream;
    private String id;

    public JournalDataManager(String username) {
        this.username = username;
        journalEntries = FXCollections.observableArrayList();
        initializeXStream();
        loadDataFromXML();
    }

    private void initializeXStream() {
        xstream = new XStream(new DomDriver());
        xstream.alias("JournalEntry", JournalEntry.class);
        xstream.alias("JournalEntries", List.class);

        // Untuk keamanan XStream
        xstream.allowTypes(new Class[] { JournalEntry.class, ArrayList.class, LocalDate.class });
        xstream.allowTypesByWildcard(new String[] { "java.time.*", "java.util.*" });
    }

    public void loadDataFromXML() {
        File dir = new File("data/journal_archive");
        if (!dir.exists())
            dir.mkdirs();

        File file = new File(generateFileName());
        if (!file.exists()) {
            journalEntries.clear();
            return;
        }

        try (FileReader reader = new FileReader(file)) {
            List<JournalEntry> loadedEntries = (List<JournalEntry>) xstream.fromXML(reader);
            journalEntries.clear();
            if (loadedEntries != null) {
                journalEntries.addAll(loadedEntries);
            }
        } catch (Exception e) {
            System.err.println("Error loading journal entries: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public ObservableList<JournalEntry> getJournalEntries() {
        return journalEntries;
    }

    public void simpanKeXML() {
        try {
            File dir = new File("data/journal_archive");
            if (!dir.exists())
                dir.mkdirs();

            String filePath = generateFileName();
            FileWriter writer = new FileWriter(filePath);
            xstream.toXML(new ArrayList<>(journalEntries), writer);
            writer.close();

            System.out.println("Journal entries saved to: " + filePath);
        } catch (IOException e) {
            System.err.println("Error saving journal entries: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void addJournalEntry(JournalEntry entry) {
        journalEntries.add(entry);
        simpanKeXML();
    }

    public void updateJournalEntry(JournalEntry updatedEntry) {
        for (int i = 0; i < journalEntries.size(); i++) {
            JournalEntry entry = journalEntries.get(i);
            if (entry.getId() == updatedEntry.getId()) {
                journalEntries.set(i, updatedEntry);
                simpanKeXML();
                break;
            }
        }
    }

    public void deleteJournalEntry(JournalEntry entry) {
        journalEntries.remove(entry);
        simpanKeXML();
    }

    public JournalEntry getJournalEntryById(int id) {
        for (JournalEntry entry : journalEntries) {
            if (entry.getId() == id) {
                return entry;
            }
        }
        return null;
    }

    private String generateFileName() {
        // Menggunakan tanggal sekarang untuk nama file
        String tanggal = LocalDate.now().toString(); // atau bisa pakai ID jurnal
        return "data/journal_archive/journal_" + username + "_archive_" + tanggal + ".xml";
    }

}