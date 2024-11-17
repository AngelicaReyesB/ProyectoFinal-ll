package co.edu.uniquindio.bookyourstay.controlador;

import co.edu.uniquindio.bookyourstay.controlador.observador.Observable;
import co.edu.uniquindio.bookyourstay.modelo.Alojamiento;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
    @FXML private HBox detalleEvento;
    @FXML private ImageView imagenAlojamiento;
    @FXML private Label nombreAlojamiento;

    @FXML
    void irInicio(ActionEvent event) {

    }
    private final PrincipalControlador principalControlador;

    public DetallesAlojamientoControlador(){
        principalControlador = PrincipalControlador.getInstancia();
    }

    public void detalleAlojamiento(){
        //principalControlador.getSesion().setAlojamientoDetalle(alojamientoRandom);
        //principalControlador.cerrarVentana(correo);
    }

    public void detalleAlojamientoBox(Alojamiento alojamiento){
        System.out.println(alojamiento);
        //principalControlador.getSesion().setAlojamientoDetalle(alojamientoRandom);
        principalControlador.navegarVentana("/detallesAlojamiento.fxml", "Detalles del alojamiento.");
        //principalControlador.cerrarVentana(correo);
    }

    public void reservarAlojamiento() {
        //principalControlador.getSesion().setReservarAlojamiento(alojamientoRandom);
        principalControlador.navegarVentana("/reservaAlojamiento.fxml", "Reservar alojamiento.");
        principalControlador.cerrarVentana(btnReservarAhora);
    }

    private void cargarDetallesAlojamiento(Alojamiento alojamiento) {
        // Establecer los valores en la vista
        nombreAlojamiento.setText(alojamiento.getNombre());
        descripcionAlojamiento.setText(alojamiento.getDescripcion());
        ValorAlojamiento.setText(String.valueOf(alojamiento.getValorNoche()));
        capacidadMaxima.setText(String.valueOf(alojamiento.getCapacidadMaxima()));
        CiudadAlojamiento.setText(alojamiento.getTipoCiudad().toString());

        // Configurar la imagen
        if (alojamiento.getImagen() != null && !alojamiento.getImagen().isEmpty()) {
            imagenAlojamiento.setImage(new Image(alojamiento.getImagen()));
        }

        // Agregar los servicios al ListView
        ListaServicios.getItems().addAll(alojamiento.getServiciosIncluidos());
    }
    @Override
    public void notificar() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Alojamiento alojamiento = principalControlador.getSesion().getAlojamientoDetalle();

        if (alojamiento != null) {
            // Configurar los detalles del alojamiento
            cargarDetallesAlojamiento(alojamiento);
        }
    }
}
