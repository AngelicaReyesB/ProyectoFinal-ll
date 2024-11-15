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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

//ponerle los nombres en el fxml
public class AlojamientosRentablesControlador implements Observable, Initializable {

    @FXML private Button btnRegresar;
    @FXML private BarChart<String, Number> barChart;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;
    private final PrincipalControlador principalControlador;

    public AlojamientosRentablesControlador(){
        principalControlador = PrincipalControlador.getInstancia();
    }

    @FXML
    private void irPanelAdministrador(ActionEvent event) {
        try {
            principalControlador.navegarVentana("/panelAdministrador.fxml", "Panel del Administrador");
        } catch (Exception e) {
            principalControlador.mostrarAlerta(e.getMessage(), Alert.AlertType.ERROR);
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
