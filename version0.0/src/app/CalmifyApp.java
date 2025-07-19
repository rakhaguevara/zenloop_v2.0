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
        // FXMLLoader(getClass().getResource("/version0.0/src/view/aiView.fxml"));
        // FXMLLoader fxmlLoader = new
        // FXMLLoader(getClass().getResource("/view/DashboardZen/journalArchive.fxml"));

        // Parent root =
        // FXMLLoader.load(getClass().getResource("/view/DashboardZen/stressStatistic.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Banner/banner.fxml"));
        Parent root = fxmlLoader.load();

        // FXMLLoader fxmlLoader = new
        // FXMLLoader(getClass().getResource("/view/DashboardZen/journalArchive.fxml"));
        // Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/view/style.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/view/card-journal.css").toExternalForm());

        stage.setScene(scene);
        stage.setResizable(false);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
