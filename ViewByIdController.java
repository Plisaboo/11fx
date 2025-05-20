package task11;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.event.ActionEvent;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class ViewByIdController {
    @FXML
    private ComboBox<String> tableCombo;
    @FXML
    private TextField idField;
    @FXML
    private TextArea resultArea;

    @FXML
    public void initialize() {
        List<String> tables = ConsoleHelperFX.getAllTables(Main11FX.conn);
        tableCombo.getItems().setAll(tables);
    }

    @FXML
    private void onShow(ActionEvent event) {
        String table = tableCombo.getValue();
        String id = idField.getText().trim();
        resultArea.clear();

        if (table == null || table.isEmpty()) {
            showError("Выберите таблицу.");
            return;
        }
        if (id.isEmpty()) {
            showError("Введите ID.");
            return;
        }

        try {
            int intId = Integer.parseInt(id);
            String query = "SELECT * FROM \"" + table + "\" WHERE id = ?";
            try (PreparedStatement ps = Main11FX.conn.prepareStatement(query)) {
                ps.setInt(1, intId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int columns = rs.getMetaData().getColumnCount();
                        StringBuilder sb = new StringBuilder();
                        for (int i = 1; i <= columns; i++) {
                            String colName = rs.getMetaData().getColumnName(i);
                            String value = rs.getString(i);
                            sb.append(colName).append(": ").append(value).append("\n");
                        }
                        resultArea.setText(sb.toString());
                    } else {
                        resultArea.setText("Запись не найдена.");
                    }
                }
            }
        } catch (NumberFormatException e) {
            showError("ID должен быть числом.");
        } catch (Exception e) {
            showError("Ошибка: " + e.getMessage());
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