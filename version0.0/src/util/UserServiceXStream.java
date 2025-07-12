package util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import model.UserData;
import model.UserList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class UserServiceXStream {

    private static final String FILE_NAME = "userdata.xml";
    private XStream xstream;

    public UserServiceXStream() {
        // Gunakan DomDriver agar tidak perlu xpp3
        xstream = new XStream(new DomDriver());

        // Allow semua class di package model
        xstream.allowTypesByWildcard(new String[] {
                "model.**"
        });

        // Alias agar XML tidak terlalu panjang
        xstream.alias("user", UserData.class);
        xstream.alias("users", UserList.class);
    }

    /**
     * Register user baru
     */
    public void registerUser(UserData user) {
        UserList userList = loadUserList();
        userList.addUser(user);
        saveUserList(userList);
        System.out.println("User registered.");
    }

    /**
     * Validasi login
     */
    public UserData loginUser(String username, String password) {
        List<UserData> users = loadUsers();

        for (UserData user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user; // Kembalikan objek UserData
            }
        }
        return null;
    }

    /**
     * Cek apakah username sudah digunakan
     */
    public boolean isUsernameTaken(String username) {
        UserList userList = loadUserList();
        for (UserData user : userList.getUsers()) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Load daftar user dari XML
     */

    public List<UserData> loadUsers() {
        return loadUserList().getUsers();
    }

    private UserList loadUserList() {
        try {
            File file = new File(FILE_NAME);
            if (!file.exists()) {
                return new UserList();
            }

            FileInputStream fis = new FileInputStream(file);
            return (UserList) xstream.fromXML(fis);
        } catch (Exception e) {
            e.printStackTrace();
            return new UserList();
        }
    }

    /**
     * Simpan daftar user ke XML
     */
    private void saveUserList(UserList userList) {
        try (FileOutputStream fos = new FileOutputStream(FILE_NAME)) {
            xstream.toXML(userList, fos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
