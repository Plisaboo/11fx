package task11;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.event.ActionEvent;

import java.sql.PreparedStatement;
import java.sql.Statement;

public class FactorialCalculationController {
    @FXML
    private TextField numberField, tableNameField;
    @FXML
    private TextArea resultArea;

    @FXML
    private void onCalculate(ActionEvent event) {
        resultArea.clear();
        String numberText = numberField.getText().trim();
        String tableName = tableNameField.getText().trim();
        int number;
        try {
            number = Integer.parseInt(numberText);
            if (number < 0) {
                showError("Число должно быть неотрицательным.");
                return;
            }
        } catch (NumberFormatException e) {
            showError("Введите корректное число.");
            return;
        }

        long result = factorial(number);
        resultArea.setText("Факториал " + number + " = " + result);

        if (tableName.isEmpty()) {
            showError("Введите имя таблицы для сохранения.");
            return;
        }

        try {
            boolean tableExists = ConsoleHelperFX.tableExists(Main11FX.conn, tableName);
            if (!tableExists) {
                recreateFactorialTable(tableName);
            } else if (!ConsoleHelperFX.hasColumns(Main11FX.conn, tableName, new String[]{"number", "result"})) {
                recreateFactorialTable(tableName);
            }
            try (PreparedStatement pstmt = Main11FX.conn.prepareStatement(
                    "INSERT INTO \"" + tableName + "\" (number, result) VALUES (?, ?)")) {
                pstmt.setInt(1, number);
                pstmt.setLong(2, result);
                pstmt.executeUpdate();
                showInfo("Результат сохранён в таблицу " + tableName);
            }
        } catch (Exception e) {
            showError("Ошибка при сохранении: " + e.getMessage());
        }
    }

    private void recreateFactorialTable(String tableName) throws Exception {
        try (Statement stmt = Main11FX.conn.createStatement()) {
            stmt.executeUpdate("DROP TABLE IF EXISTS \"" + tableName + "\"");
            stmt.executeUpdate("CREATE TABLE \"" + tableName + "\" (number INTEGER, result BIGINT)");
        }
    }

    private long factorial(int n) {
        long res = 1;
        for (int i = 2; i <= n; i++) {
            res *= i;
        }
        return res;
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