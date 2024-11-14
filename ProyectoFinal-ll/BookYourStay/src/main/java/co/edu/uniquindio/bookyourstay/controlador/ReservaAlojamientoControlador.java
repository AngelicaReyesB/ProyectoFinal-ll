package co.edu.uniquindio.bookyourstay.controlador;

import co.edu.uniquindio.bookyourstay.controlador.observador.Observable;
import co.edu.uniquindio.bookyourstay.modelo.Alojamiento;
import co.edu.uniquindio.bookyourstay.modelo.Cliente;
import co.edu.uniquindio.bookyourstay.modelo.Factura;
import co.edu.uniquindio.bookyourstay.modelo.Reserva;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javax.swing.text.html.ImageView;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ReservaAlojamientoControlador implements Observable, Initializable {


    @FXML private Button btnPagar;
    @FXML private Button btnRegresar;
    @FXML private Label ciudadAlojamiento;
    @FXML private Label capacidadAlojamiento;
    @FXML private ImageView imagenEvento;
    @FXML private Label nombreAlojamiento;
    @FXML private Label verDescuento;
    @FXML private Label verSubtotal;
    @FXML private Label verTotal;
    @FXML private DatePicker fechaInicioPicker;
    @FXML private DatePicker fechaFinPicker;
    @FXML private ComboBox<Integer> cbNumHuespedes;
    private final PrincipalControlador principalControlador;
    private Alojamiento alojamiento;

    public ReservaAlojamientoControlador(){
        principalControlador = PrincipalControlador.getInstancia();
        Cliente cliente = principalControlador.getSesion().getCliente();
        alojamiento = principalControlador.getSesion().getReservarAlojamiento();
        System.out.println("COMPRA:" + cliente);
        System.out.println("COMPRA:" + alojamiento);
    }

    private void obtenerAlojamientoAleatorio(){
        alojamiento = principalControlador.getSesion().getReservarAlojamiento();
        nombreAlojamiento.setText(alojamiento.getNombre());
        ciudadAlojamiento.setText(alojamiento.getTipoCiudad().toString());
        capacidadAlojamiento.setText(String.valueOf(alojamiento.getCapacidadMaxima()));
        //serviciosAlojamiento.setText(alojamiento.getServiciosIncluidos());
    }

    private LocalDate obtenerFechaInicio() {
        return fechaInicioPicker.getValue();
    }

    private LocalDate obtenerFechaFin() {
        return fechaFinPicker.getValue();
    }

    private int obtenerNumeroHuespedes() {
        return cbNumHuespedes.getValue();
    }


    @FXML
    void confirmaReserva() {
        try {
            Cliente cliente = principalControlador.getSesion().getCliente();

            if (cliente == null) {
                principalControlador.mostrarAlerta("Debe iniciar sesión para confirmar la reserva.", Alert.AlertType.ERROR);
                return;
            }

            //Alojamiento alojamiento = obtenerAlojamientoSeleccionado(); // Implementa este método para obtener el alojamiento de la interfaz
            if (alojamiento == null || !alojamiento.isActivo()) {
                principalControlador.mostrarAlerta("El alojamiento seleccionado no está disponible.", Alert.AlertType.ERROR);
                return;
            }

            LocalDate fechaInicio = obtenerFechaInicio();
            LocalDate fechaFin = obtenerFechaFin();
            int numHuespedes = obtenerNumeroHuespedes();

            Factura factura = null;

            Reserva reserva = principalControlador.realizarReserva(cliente, alojamiento, fechaInicio, fechaFin, numHuespedes, factura);

            if (reserva != null) {

                factura = principalControlador.generarFactura(reserva);
                reserva.setFactura(factura);
                reserva.setPagado(true); // Marcamos como pagado si corresponde

                String codigoQR = principalControlador.enviarCodigoQR(factura, cliente.getEmail());
                reserva.setCodigoQR(codigoQR);

                principalControlador.mostrarAlerta("Reserva confirmada exitosamente. Se ha enviado un correo con el QR.", Alert.AlertType.INFORMATION);
            } else {
                principalControlador.mostrarAlerta("No se pudo completar la reserva. Inténtelo más tarde.", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            principalControlador.mostrarAlerta("Ocurrió un error al confirmar la reserva: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    @FXML
    void irInicio() {
        principalControlador.getSesion().setReservarAlojamiento(null);
        principalControlador.getSesion().setClientes(null);
        principalControlador.navegarVentana("/inicio.fxml", "Inicio.");
        principalControlador.cerrarVentana(btnRegresar);
    }

    //private void mostrarUsuario(){
      //  if(cliente == null){
        //    nombreUsuario.setDisable(true);
        //}else {
          //nombreUsuario.setDisable(true);
            //nombreUsuario.setPromptText(cliente.getNombreCompleto());
            //btnIngresar.setDisable(true);
    //}

    @Override
    public void notificar() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        obtenerAlojamientoAleatorio();
        cbNumHuespedes.setItems(FXCollections.observableArrayList());
        //mostrarUsuario();
    }
}
