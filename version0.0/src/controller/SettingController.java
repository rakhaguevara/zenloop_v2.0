package controller;

import java.io.IOException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import model.SessionManager;
import model.UserData;
import model.UserList;
import util.UserServiceXStream;
import util.AlertUtil;

public class SettingController {

    @FXML
    private TextField tfUsername, tfEmail, tfName, tfPhoneNum, tfPassword;

    @FXML
    private PasswordField tfChangePass;

    @FXML
    private Circle ccProfileImg;

    @FXML
    private Button btnSaveChanges, btnLogOutSet, btnDeleteAccount;

    private UserServiceXStream userService = new UserServiceXStream();

    @FXML
    public void initialize() {
        UserData currentUser = SessionManager.getCurrentUser();

        // Set semua field utama non-editable
        tfPassword.setEditable(false);

        if (currentUser != null) {
            tfUsername.setText(currentUser.getUsername());
            tfEmail.setText(currentUser.getEmail());
            tfName.setText(currentUser.getNama());
            tfPhoneNum.setText(currentUser.getPhone() != null ? currentUser.getPhone() : "");
            tfPassword.setText(currentUser.getPassword());
        } else {
            System.err.println("⚠️ Tidak ada user yang login.");
        }
    }

    @FXML
    void handleSaveChanges(ActionEvent event) {
        UserData currentUser = SessionManager.getCurrentUser();
        if (currentUser == null)
            return;

        // Ambil data dari field
        String newName = tfName.getText().trim();
        String newEmail = tfEmail.getText().trim();
        String newPhone = tfPhoneNum.getText().trim();
        String newPass = tfChangePass.getText().trim();

        boolean updated = false;

        if (!newName.equals(currentUser.getNama())) {
            currentUser.setNama(newName);
            updated = true;
        }

        if (!newEmail.equals(currentUser.getEmail())) {
            currentUser.setEmail(newEmail);
            updated = true;
        }

        if (!newPhone.equals(currentUser.getPhone())) {
            currentUser.setPhone(newPhone);
            updated = true;
        }

        if (!newPass.isEmpty() && !newPass.equals(currentUser.getPassword())) {
            currentUser.setPassword(newPass);
            tfPassword.setText(newPass); // update tampilan password
            updated = true;
        }

        if (updated) {
            userService.saveUser(currentUser);
            tfChangePass.clear();
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Berhasil", "Data berhasil diperbarui.");
        } else {
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Tidak Ada Perubahan", "Tidak ada data yang diubah.");
        }
    }

    // public void saveUser(UserData updatedUser) {
    // UserList userList = loadUserList();
    // List<UserData> users = userList.getUsers();

    // for (int i = 0; i < users.size(); i++) {
    // UserData user = users.get(i);
    // if (user.getUsername().equals(updatedUser.getUsername())) {
    // // Update data satu per satu (lebih aman daripada replace objek)
    // user.setNama(updatedUser.getNama());
    // user.setEmail(updatedUser.getEmail());
    // user.setPhone(updatedUser.getPhone());
    // user.setPassword(updatedUser.getPassword());
    // break;
    // }
    // }

    // saveUserList(userList);
    // }

    @FXML
    void handleChangeDeleteAccount(ActionEvent event) {
        UserData currentUser = SessionManager.getCurrentUser();
        if (currentUser == null)
            return;

        // 1. Hapus user dari XML
        userService.deleteUser(currentUser.getUsername());

        // 2. Logout (hapus session)
        SessionManager.logout();

        // 3. Tampilkan alert
        AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Akun Dihapus", "Akun Anda berhasil dihapus.");

        // 4. Redirect ke login.fxml
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login/loginZenloopFXML.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) btnDeleteAccount.getScene().getWindow();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleLogOut(ActionEvent event) {
        SessionManager.logout();
        AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Logout", "Anda berhasil logout.");
        // TODO: Redirect ke login page
    }

    // Fungsi tidak digunakan tapi tetap ada jika dipanggil dari FXML
    @FXML
    void handleCancelSett(ActionEvent event) {

        System.out.println("cancel set");
    }

    @FXML
    void handleLoginSetting(ActionEvent event) {
    }

    @FXML
    void handleChangeName(ActionEvent event) {
    }

    @FXML
    void handleChangeEmail(ActionEvent event) {
    }

    @FXML
    void handleChangePhoneNum(ActionEvent event) {
    }
}
