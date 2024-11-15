package co.edu.uniquindio.bookyourstay.controlador;

import co.edu.uniquindio.bookyourstay.controlador.observador.Observable;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter

public class AlojamientosRentablesControlador implements Observable, Initializable {

    @FXML
    private Button btnRegresar;

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private void irPanelAdministrador(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/panelAdministrador.fxml"));
            Parent adminRoot = loader.load();

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene adminScene = new Scene(adminRoot);

            currentStage.setScene(adminScene);
            currentStage.show();

            System.out.println("Ventana cambiada al panel del administrador");

        } catch (IOException e) {
            System.err.println("Error al cargar el panel de administración: " + e.getMessage());
        }
    }

    // Metodo para el grafico con los datod de alojamiento
     @FXML
    public void initialize() {
        // Configuración de los ejes del gráfico
        xAxis.setLabel("Tipo de Alojamiento");
        yAxis.setLabel("Ganancias");

        // Crear datos de ejemplo para el gráfico de barras
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Rentabilidad de Tipos de Alojamientos");

        series.getData().add(new XYChart.Data<>("Casa", 12000));
        series.getData().add(new XYChart.Data<>("Apartamento", 8000));
        series.getData().add(new XYChart.Data<>("Hotel", 1500));

        barChart.getData().add(series);
    }

    @Override
    public void notificar() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
