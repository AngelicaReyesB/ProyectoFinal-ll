package co.edu.uniquindio.bookyourstay.controlador;

import co.edu.uniquindio.bookyourstay.controlador.observador.Observable;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.EventObject;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import lombok.AllArgsConstructor;

public class EstadisticasControlador implements Observable, Initializable {

    @FXML private Button btnRegresar;
    @FXML private PieChart pieChartOcupacion;
    @FXML private PieChart pieChartGanancias;
    private final PrincipalControlador principalControlador;

    public EstadisticasControlador(){
        principalControlador = PrincipalControlador.getInstancia();
    }

    private void cargarDatosOcupacion() {
        ObservableList<PieChart.Data> datosOcupacion = FXCollections.observableArrayList(
                new PieChart.Data("Casa", 35),
                new PieChart.Data("Apartamento", 45),
                new PieChart.Data("Hotel", 20)
        );
        pieChartOcupacion.setData(datosOcupacion);
    }

    private void cargarDatosGanancias() {
        ObservableList<PieChart.Data> datosGanancias = FXCollections.observableArrayList(
                new PieChart.Data("Casa", 500000),
                new PieChart.Data("Apartamento", 700000),
                new PieChart.Data("Hotel", 300000)
        );
        pieChartGanancias.setData(datosGanancias);
    }

    @FXML
    private void irPanelInicio() {
        try {
            principalControlador.navegarVentana("/panelAdministrador.fxml", "Panel del administrador");
            principalControlador.cerrarVentana(btnRegresar);
        } catch (Exception e) {
            principalControlador.mostrarAlerta(e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @Override
    public void notificar() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarDatosOcupacion();
        cargarDatosGanancias();
    }
}
