package app;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class CalmifyApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Pastikan path-nya sesuai dengan struktur: /view/login/...
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/login/loginCalmifyFXML.fxml"));

        // stage.setResizable(false);

        Scene scene = new Scene(fxmlLoader.load());

        // background image
        ImageView backgroundImage = (ImageView) fxmlLoader.getNamespace().get("backgroundImage");
        Image image = new Image("/app/resource/right.png"); // Ganti dengan path gambar Anda
        backgroundImage.setImage(image);

        // Tambahkan style.css dari dalam /view/login/
        scene.getStylesheets().add(getClass().getResource("/view/login/style.css").toExternalForm());

        stage.setTitle("Login Page Calmify");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
