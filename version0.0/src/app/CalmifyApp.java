package app;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CalmifyApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        // FXMLLoader fxmlLoader = new
        // FXMLLoader(getClass().getResource("/view/homeDr.fxml"));

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Banner/banner.fxml"));
        Parent root = fxmlLoader.load();

        // Buat scene dengan ukuran spesifik (misalnya 1200 x 700)
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/view/style.css").toExternalForm());

        // Tambahkan style.css jika ada
        // scene.getStylesheets().add(getClass().getResource("/view/style.css").toExternalForm());

        stage.setScene(scene);

        // Jika ingin agar user tidak bisa resize:
        stage.setResizable(false);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
