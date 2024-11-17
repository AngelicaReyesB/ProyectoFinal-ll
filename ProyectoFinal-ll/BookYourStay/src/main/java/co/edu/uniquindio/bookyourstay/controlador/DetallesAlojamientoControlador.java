package co.edu.uniquindio.bookyourstay.controlador;

import co.edu.uniquindio.bookyourstay.controlador.observador.Observable;
import co.edu.uniquindio.bookyourstay.modelo.Alojamiento;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class DetallesAlojamientoControlador implements Observable, Initializable {

    @FXML private Label CiudadAlojamiento;
    @FXML private Label labelServicios;
    @FXML private Label ValorAlojamiento;
    @FXML private Button btnRegresar;
    @FXML private Button btnReservarAhora;
    @FXML private Label capacidadMaxima;
    @FXML private Label descripcionAlojamiento;
    @FXML private ImageView imagenAlojamiento;
    @FXML private Label nombreAlojamiento;

    @FXML
    void irInicio(ActionEvent event) {

    }
    private final PrincipalControlador principalControlador;

    public DetallesAlojamientoControlador(){
        principalControlador = PrincipalControlador.getInstancia();
    }

    public void reservarAlojamiento() {
        Alojamiento alojamientoSeleccionado = principalControlador.getSesion().getAlojamientoDetalle();
        if (alojamientoSeleccionado != null) {
            principalControlador.getSesion().setReservarAlojamiento(alojamientoSeleccionado);
            principalControlador.navegarVentana("/reservaAlojamiento.fxml", "Reservar alojamiento.");
            principalControlador.cerrarVentana(btnReservarAhora);
        } else {
            principalControlador.mostrarAlerta("No se ha seleccionado un alojamiento.", Alert.AlertType.ERROR);
        }
    }

    private void cargarDetallesAlojamiento(Alojamiento alojamiento) {
        nombreAlojamiento.setText(alojamiento.getNombre());
        descripcionAlojamiento.setText(alojamiento.getDescripcion());
        ValorAlojamiento.setText(String.valueOf(alojamiento.getValorNoche()));
        capacidadMaxima.setText(String.valueOf(alojamiento.getCapacidadMaxima()));
        CiudadAlojamiento.setText(alojamiento.getTipoCiudad().toString());

        if (alojamiento.getImagen() != null && !alojamiento.getImagen().isEmpty()) {
            imagenAlojamiento.setImage(new Image(alojamiento.getImagen()));
        }

        StringBuilder serviciosText = new StringBuilder("Servicios incluidos:\n");
        for (String servicio : alojamiento.getServiciosIncluidos()) {
            serviciosText.append("- ").append(servicio).append("\n");
        }

        labelServicios.setText(serviciosText.toString());
    }

    @Override
    public void notificar() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Alojamiento alojamiento = principalControlador.getSesion().getAlojamientoDetalle();

        if (alojamiento != null) {
            cargarDetallesAlojamiento(alojamiento);
        }
    }
}
