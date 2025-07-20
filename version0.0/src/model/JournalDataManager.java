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
    private static final String XML_FILE = "journal_entries.xml";
    private ObservableList<JournalEntry> journalEntries;
    private XStream xstream;
    private String id;

    public JournalDataManager() {
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
        File file = new File(XML_FILE);
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

    public void simpanKeXML() {
        try (FileWriter writer = new FileWriter(XML_FILE)) {
            xstream.toXML(new ArrayList<>(journalEntries), writer);
            System.out.println("Journal entries saved successfully to " + XML_FILE);
        } catch (IOException e) {
            System.err.println("Error saving journal entries: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public ObservableList<JournalEntry> getJournalEntries() {
        return journalEntries;
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

}