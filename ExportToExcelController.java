package task11;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.event.ActionEvent;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class ExportToExcelController {
    @FXML
    private ComboBox<String> tableCombo;
    @FXML
    private TextField fileNameField;

    @FXML
    public void initialize() {
        List<String> tables = ConsoleHelperFX.getAllTables(Main11FX.conn);
        tableCombo.getItems().setAll(tables);
    }

    @FXML
    private void onExport(ActionEvent event) {
        String tableName = tableCombo.getValue();
        String baseFileName = fileNameField.getText().trim();
        if (tableName == null || tableName.isEmpty()) {
            showError("Выберите таблицу.");
            return;
        }
        if (baseFileName.isEmpty()) {
            showError("Введите имя файла.");
            return;
        }
        String fileName = baseFileName + ".xlsx";
        File file = new File(fileName);

        if (file.exists()) {
            Alert overwriteAlert = new Alert(Alert.AlertType.CONFIRMATION, "Файл уже существует. Перезаписать?", ButtonType.YES, ButtonType.NO);
            overwriteAlert.showAndWait();
            if (overwriteAlert.getResult() != ButtonType.YES) {
                return;
            }
        }

        try (Statement stmt = Main11FX.conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM \"" + tableName + "\"");
             Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet(tableName);
            Row header = sheet.createRow(0);
            int colCount = rs.getMetaData().getColumnCount();

            for (int i = 1; i <= colCount; i++) {
                Cell cell = header.createCell(i - 1);
                cell.setCellValue(rs.getMetaData().getColumnName(i));
            }

            int rowIndex = 1;
            while (rs.next()) {
                Row row = sheet.createRow(rowIndex++);
                for (int i = 1; i <= colCount; i++) {
                    row.createCell(i - 1).setCellValue(rs.getString(i));
                }
            }

            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
                showInfo("Данные успешно экспортированы в " + fileName);
            }
        } catch (Exception e) {
            showError("Ошибка при экспорте: " + e.getMessage());
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