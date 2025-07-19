package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class JournalEntry {
    private int id;
    private String title;
    private String content;
    private String mood;
    private LocalDate date;
    private static int nextId = 1;

    // Constructor default
    public JournalEntry() {
        this.id = nextId++;
        this.date = LocalDate.now();
    }

    // Constructor dengan parameter
    public JournalEntry(String title, String content, String mood, LocalDate date) {
        this.id = nextId++;
        this.title = title;
        this.content = content;
        this.mood = mood;
        this.date = date;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        if (id >= nextId) {
            nextId = id + 1;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getFormattedDate() {
        return date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
    }

    public String getPreview() {
        if (content == null || content.trim().isEmpty()) {
            return "No content...";
        }
        return content.length() > 50 ? content.substring(0, 50) + "..." : content;
    }

    @Override
    public String toString() {
        return title + " - " + getFormattedDate();
    }
}