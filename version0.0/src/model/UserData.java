package model;

import java.util.ArrayList;
import java.util.List;

public class UserData {
    private String nama, username, email, password, role, phone, address;
    private List<StressHarian> stressList = new ArrayList<>();
    private List<Song> songList = new ArrayList<>();

    public List<StressHarian> getStressList() {
        return stressList;
    }

    public UserData(String username, String email, String password, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setStressList(List<StressHarian> stressList) {
        this.stressList = stressList;
    }

    public List<Song> getSongList() {
        return songList;
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
    }

    public void addStressData(StressHarian stressData) {
        this.stressList.add(stressData);
    }

    public void addSong(Song song) {
        this.songList.add(song);
    }

    public String getRole() {
        return role;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getNama() {
        return nama;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
