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
        xstream = new XStream(new DomDriver());

        xstream.allowTypesByWildcard(new String[] {
                "model.**"
        });

        xstream.alias("user", UserData.class);
        xstream.alias("users", UserList.class);
    }

    /**
     * Mendaftarkan user baru dan menyimpannya ke file.
     */
    public void registerUser(UserData user) {
        UserList userList = loadUserList();
        userList.addUser(user);
        saveUserList(userList);
        System.out.println("✅ User registered successfully.");
    }

    public UserData loginUser(String username, String password) {
        List<UserData> users = loadUsers();
        for (UserData user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Mengecek apakah username sudah terdaftar.
     */
    public boolean isUsernameTaken(String username) {
        for (UserData user : loadUsers()) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Mengembalikan seluruh user dari file XML.
     */
    public List<UserData> loadUsers() {
        return loadUserList().getUsers();
    }

    /**
     * Mengembalikan daftar user yang memiliki role tertentu.
     */
    public List<UserData> getUsersByRole(String role) {
        List<UserData> filtered = new ArrayList<>();
        for (UserData user : loadUsers()) {
            if (user.getRole() != null && user.getRole().equalsIgnoreCase(role)) {
                filtered.add(user);
            }
        }
        return filtered;
    }

    /**
     * Menyimpan ulang data user yang sudah diupdate (data stress/song dsb).
     */
    public void saveUser(UserData updatedUser) {
        UserList userList = loadUserList();
        List<UserData> users = userList.getUsers();

        for (int i = 0; i < users.size(); i++) {
            UserData user = users.get(i);
            if (user.getUsername().equals(updatedUser.getUsername())) {
                user.setNama(updatedUser.getNama());
                user.setEmail(updatedUser.getEmail());
                user.setPhone(updatedUser.getPhone());
                user.setPassword(updatedUser.getPassword());
                user.setProfileImagePath(updatedUser.getProfileImagePath()); // ✅ tambahkan ini
                break;
            }
        }

        saveUserList(userList);
    }

    /**
     * Mengambil UserList lengkap dari file.
     */
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
     * Menyimpan seluruh UserList ke file XML.
     */
    private void saveUserList(UserList userList) {
        try (FileOutputStream fos = new FileOutputStream(FILE_NAME)) {
            xstream.toXML(userList, fos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(String username) {
        List<UserData> allUsers = loadUsers(); // ✅ pakai method yang sudah ada
        allUsers.removeIf(u -> u.getUsername().equalsIgnoreCase(username));

        UserList updatedList = new UserList();
        updatedList.setUsers(allUsers);
        saveUserList(updatedList);
    }

}
