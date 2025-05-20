package task11;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.event.ActionEvent;

import java.sql.Statement;
import java.util.List;

public class DropTableController {
    @FXML
    private ComboBox<String> tableCombo;
    @FXML
    private TextArea resultArea;

    @FXML
    public void initialize() {
        refreshTables();
    }

    private void refreshTables() {
        List<String> tables = ConsoleHelperFX.getAllTables(Main11FX.conn);
        tableCombo.getItems().setAll(tables);
    }

    @FXML
    private void onDrop(ActionEvent event) {
        resultArea.clear();
        String table = tableCombo.getValue();
        if (table == null || table.isEmpty()) {
            showError("Выберите таблицу.");
            return;
        }
        try (Statement stmt = Main11FX.conn.createStatement()) {
            stmt.executeUpdate("DROP TABLE IF EXISTS \"" + table + "\"");
            resultArea.setText("Таблица " + table + " удалена.");
            refreshTables();
        } catch (Exception e) {
            showError("Ошибка при удалении: " + e.getMessage());
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
}