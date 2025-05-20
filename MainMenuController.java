package task11;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class MainMenuController {

    @FXML
    private void onCreateEmptyTable(ActionEvent event) throws Exception {
        showScene("CreateEmptyTable.fxml", event);
    }

    @FXML
    private void onShowTables(ActionEvent event) {
        try {
            var tables = ConsoleHelperFX.getAllTables(Main11FX.conn);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Таблицы");
            alert.setHeaderText("Список таблиц в базе данных");
            alert.setContentText(String.join("\n", tables));
            alert.showAndWait();
        } catch (Exception e) {
            showError("Ошибка получения таблиц: " + e.getMessage());
        }
    }

    @FXML
    private void onViewById(ActionEvent event) throws Exception {
        showScene("ViewById.fxml", event);
    }

    @FXML
    private void onTriangle(ActionEvent event) throws Exception {
        showScene("TriangleCalculation.fxml", event);
    }

    @FXML
    private void onFactorial(ActionEvent event) throws Exception {
        showScene("FactorialCalculation.fxml", event);
    }

    @FXML
    private void onExportExcel(ActionEvent event) throws Exception {
        showScene("ExportToExcel.fxml", event);
    }

    @FXML
    private void onDropTable(ActionEvent event) throws Exception {
        showScene("DropTable.fxml", event);
    }

    @FXML
    private void onExit(ActionEvent event) {
        ((Stage) ((javafx.scene.Node)event.getSource()).getScene().getWindow()).close();
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg);
        alert.showAndWait();
    }

    private void showScene(String fxml, ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) ((javafx.scene.Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }
}