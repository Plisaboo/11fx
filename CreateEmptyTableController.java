package task11;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.event.ActionEvent;

import java.sql.Statement;

public class CreateEmptyTableController {
    @FXML
    private TextField tableNameField;

    @FXML
    private void onCreate(ActionEvent event) {
        String tableName = tableNameField.getText().trim();
        if (tableName.isEmpty()) {
            showError("Введите имя таблицы.");
            return;
        }
        try (Statement stmt = Main11FX.conn.createStatement()) {
            stmt.executeUpdate("CREATE TABLE \"" + tableName + "\" (id SERIAL PRIMARY KEY)");
            showInfo("Пустая таблица \"" + tableName + "\" успешно создана.");
            tableNameField.clear();
        } catch (Exception e) {
            showError("Ошибка при создании таблицы: " + e.getMessage());
        }
    }

    @FXML
    private void onBack(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((javafx.scene.Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg);
        alert.showAndWait();
    }
    private void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg);
        alert.showAndWait();
    }
}