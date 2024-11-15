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
import javafx.stage.Stage;
import lombok.AllArgsConstructor;

@AllArgsConstructor

public class EstadisticasControlador implements Observable, Initializable {

    @FXML
    private PieChart pieChartOcupacion;

    @FXML
    private PieChart pieChartGanancias;

    @FXML
    private void initialize() {
        cargarDatosOcupacion();
        cargarDatosGanancias();
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

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/panelAdministrador.fxml"));
            Parent adminRoot = loader.load();

            EventObject event = null;
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene adminScene = new Scene(adminRoot);

            currentStage.setScene(adminScene);
            currentStage.show();

            System.out.println("Ventana cambiada al panel del administrador");

        } catch (IOException e) {
            System.err.println("Error al cargar el panel de administración: " + e.getMessage());
        }
    }

    @Override
    public void notificar() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
