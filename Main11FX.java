package task11;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;

public class Main11FX extends Application {
    public static Connection conn;

    @Override
    public void start(Stage primaryStage) throws Exception {
        String DB_URL = "jdbc:postgresql://localhost:5432/task11";
        String USER = "postgres";
        String PASS = "postgres";
        conn = DriverManager.getConnection(DB_URL, USER, PASS);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setTitle("Task11FX - Главное меню");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}