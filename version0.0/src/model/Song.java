package model;

public class Song {

    private String title;
    private String path;

    public Song() {
    }

    public Song(String title, String path) {
        this.title = title;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public String getTitle() {
        return title;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
