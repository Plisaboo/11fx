package task11;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.event.ActionEvent;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class TriangleCalculationController {
    @FXML
    private TextField fieldA, fieldB, fieldC, tableNameField;
    @FXML
    private TextArea resultArea;

    @FXML
    private void onCalculate(ActionEvent event) {
        String aText = fieldA.getText().trim();
        String bText = fieldB.getText().trim();
        String cText = fieldC.getText().trim();
        String tableName = tableNameField.getText().trim();

        resultArea.clear();

        double a, b, c;
        try {
            a = Double.parseDouble(aText);
            b = Double.parseDouble(bText);
            c = Double.parseDouble(cText);
        } catch (NumberFormatException e) {
            showError("Введите корректные значения сторон.");
            return;
        }

        Triangle triangle = new Triangle(a, b, c);
        if (!triangle.exists()) {
            showError("Указанные стороны не могут образовать треугольник.");
            return;
        }

        double perimeter = triangle.getPerimeter();
        double area = triangle.getArea();
        boolean isRight = triangle.isRight();

        resultArea.setText(String.format("Периметр: %.2f\nПлощадь: %.2f\nПрямоугольный: %s",
                perimeter, area, isRight ? "Да" : "Нет"));

        if (tableName.isEmpty()) {
            showError("Введите имя таблицы для сохранения.");
            return;
        }

        try {
            boolean tableExists = ConsoleHelperFX.tableExists(Main11FX.conn, tableName);
            if (!tableExists) {
                recreateTriangleTable(tableName);
            } else if (!ConsoleHelperFX.hasColumns(Main11FX.conn, tableName,
                    new String[]{"a", "b", "c", "perimeter", "area", "is_right"})) {
                recreateTriangleTable(tableName);
            }
            try (PreparedStatement pstmt = Main11FX.conn.prepareStatement(
                    "INSERT INTO \"" + tableName + "\" (a, b, c, perimeter, area, is_right) VALUES (?, ?, ?, ?, ?, ?)")) {
                pstmt.setDouble(1, a);
                pstmt.setDouble(2, b);
                pstmt.setDouble(3, c);
                pstmt.setDouble(4, perimeter);
                pstmt.setDouble(5, area);
                pstmt.setBoolean(6, isRight);
                pstmt.executeUpdate();
                showInfo("Результат сохранён в таблицу " + tableName);
            }
        } catch (Exception e) {
            showError("Ошибка при сохранении: " + e.getMessage());
        }
    }

    private void recreateTriangleTable(String tableName) throws SQLException {
        try (Statement stmt = Main11FX.conn.createStatement()) {
            stmt.executeUpdate("DROP TABLE IF EXISTS \"" + tableName + "\"");
            stmt.executeUpdate("CREATE TABLE \"" + tableName + "\" (" +
                    "a DOUBLE PRECISION, " +
                    "b DOUBLE PRECISION, " +
                    "c DOUBLE PRECISION, " +
                    "perimeter DOUBLE PRECISION, " +
                    "area DOUBLE PRECISION, " +
                    "is_right BOOLEAN)");
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