package model;

import java.util.ArrayList;
import java.util.List;

public class UserData {
    private String nama, username, email, password, role, phone, address;

    // untuk xtream

    public UserData() {

    }

    // Logiin Needed
    public UserData(String username, String email, String password, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Profile
    public UserData(String nama, String username, String email, String password, String role, String phone,
            String address) {
        this.nama = nama;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.phone = phone;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNama() {
        return nama;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }
}
