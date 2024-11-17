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
import javafx.scene.image.Image;

import javafx.scene.image.ImageView;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//falta
public class ReservaAlojamientoControlador implements Observable, Initializable {

    @FXML private Button btnRegresar;
    @FXML private Label ciudadAlojamiento;
    @FXML private Label capacidadAlojamiento;
    @FXML private ImageView imagen;
    @FXML private Label nombreAlojamiento;
    @FXML private DatePicker fechaInicioPicker;
    @FXML private DatePicker fechaFinPicker;
    @FXML private ComboBox<Integer> cbNumHuespedes;
    private final PrincipalControlador principalControlador;
    private Alojamiento alojamiento;

    public ReservaAlojamientoControlador() {
        principalControlador = PrincipalControlador.getInstancia();
        Cliente cliente = principalControlador.getSesion().getCliente();
        alojamiento = principalControlador.getSesion().getReservarAlojamiento();
        System.out.println("COMPRA:" + cliente);
        System.out.println("COMPRA:" + alojamiento);
    }

    private void obtenerAlojamiento() {
        alojamiento = principalControlador.getSesion().getReservarAlojamiento();
        nombreAlojamiento.setText(alojamiento.getNombre());
        ciudadAlojamiento.setText(alojamiento.getTipoCiudad().toString());
        capacidadAlojamiento.setText(String.valueOf(alojamiento.getCapacidadMaxima()));
        imagen.setImage(new Image(alojamiento.getImagen()));
    }



    private LocalDate obtenerFechaInicio() {
        return fechaInicioPicker.getValue();
    }

    private LocalDate obtenerFechaFin() {
        return fechaFinPicker.getValue();
    }

    private int obtenerNumeroHuespedes() {
        int numHuespedes = cbNumHuespedes.getValue();
        if (numHuespedes < 1 || numHuespedes > alojamiento.getCapacidadMaxima()) {
            principalControlador.mostrarAlerta("El número de huéspedes no es válido.", Alert.AlertType.ERROR);
            return -1; // O manejar el error de acuerdo a tus necesidades
        }
        return numHuespedes;
    }

    //@FXML
    //    public void realizarPago() {
    //        try {
    //            Cliente cliente = principalControlador.getSesion().getCliente();
    //            if (cliente == null) {
    //                principalControlador.mostrarAlerta("Debe iniciar sesión para confirmar la reserva.", Alert.AlertType.ERROR);
    //                return;
    //            }
    //            if (alojamiento == null || !alojamiento.isActivo()) {
    //                principalControlador.mostrarAlerta("El alojamiento seleccionado no está disponible.", Alert.AlertType.ERROR);
    //                return;
    //            }
    //            LocalDate fechaInicio = obtenerFechaInicio();
    //            LocalDate fechaFin = obtenerFechaFin();
    //            int numHuespedes = obtenerNumeroHuespedes();
    //            Factura factura = null;
    //            Reserva reserva = principalControlador.realizarReserva(cliente, alojamiento, fechaInicio, fechaFin, numHuespedes, factura);
    //
    //            if (reserva != null) {
    //                factura = principalControlador.generarFactura(reserva);
    //                reserva.setFactura(factura);
    //                reserva.setPagado(true);
    //                String codigoQR = principalControlador.enviarFacturaQR(reserva);
    //                reserva.setCodigoQR(codigoQR);
    //                principalControlador.mostrarAlerta("Reserva confirmada exitosamente. Se ha enviado un correo con el QR.", Alert.AlertType.INFORMATION);
    //            } else {
    //                principalControlador.mostrarAlerta("No se pudo completar la reserva. Inténtelo más tarde.", Alert.AlertType.ERROR);
    //            }
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //            principalControlador.mostrarAlerta("Ocurrió un error al confirmar la reserva: " + e.getMessage(), Alert.AlertType.ERROR);
    //        }
    //    }

    @FXML
    void realizarPago() {
        try {
            Cliente cliente = principalControlador.getSesion().getCliente();

            if (cliente == null) {
                principalControlador.mostrarAlerta("Debe iniciar sesión para confirmar la reserva.", Alert.AlertType.ERROR);
                return;
            }
            if (alojamiento == null || !alojamiento.isActivo()) {
                principalControlador.mostrarAlerta("El alojamiento seleccionado no está disponible.", Alert.AlertType.ERROR);
                return;
            }

            LocalDate fechaInicio = obtenerFechaInicio();
            LocalDate fechaFin = obtenerFechaFin();
            int numHuespedes = obtenerNumeroHuespedes();

            // Realizar la reserva
            Factura factura = null;
            Reserva reserva = principalControlador.realizarReserva(cliente, alojamiento, fechaInicio, fechaFin, numHuespedes, factura);
            if (reserva == null) {
                principalControlador.mostrarAlerta("No se pudo completar la reserva. Inténtelo más tarde.", Alert.AlertType.ERROR);
                return;
            }

            float costoReserva = principalControlador.calcularCostoReserva(reserva);
            verSubtotal.setText(String.valueOf(costoReserva));

            // Generar factura
            factura = principalControlador.generarFactura(reserva);
            reserva.setFactura(factura);

            // Verificar saldo disponible
            String resultado = principalControlador.verificarSaldoDisponible(cliente, factura.getTotal());
            if (resultado.contains("Saldo suficiente")) {
                // Realizar el pago
                cliente.getBilleteraVirtual().setMontoTotal(
                        cliente.getBilleteraVirtual().getMontoTotal() - factura.getTotal()
                );
                reserva.setPagado(true);

                // Enviar código QR
                String codigoQR = principalControlador.enviarFacturaQR(reserva);
                reserva.setCodigoQR(codigoQR);

                principalControlador.mostrarAlerta("Reserva confirmada exitosamente. Se ha enviado un correo con el QR.", Alert.AlertType.INFORMATION);
            } else {
                principalControlador.mostrarAlerta("Saldo insuficiente: " + resultado, Alert.AlertType.ERROR);
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

    @Override
    public void notificar() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        obtenerAlojamiento();

        // Obtener la capacidad máxima del alojamiento
        int capacidadMaxima = alojamiento.getCapacidadMaxima();

        // Crear una lista de números que va de 1 a la capacidad máxima
        cbNumHuespedes.setItems(FXCollections.observableArrayList(
                IntStream.rangeClosed(1, capacidadMaxima)
                        .boxed()
                        .collect(Collectors.toList())
        ));

        // Establecer el valor predeterminado en 1 (puedes ajustarlo según tus necesidades)
        cbNumHuespedes.getSelectionModel().selectFirst();
    }

}
