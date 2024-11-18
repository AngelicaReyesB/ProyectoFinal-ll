package co.edu.uniquindio.bookyourstay.controlador;

import co.edu.uniquindio.bookyourstay.controlador.observador.Observable;
import co.edu.uniquindio.bookyourstay.modelo.Alojamiento;
import co.edu.uniquindio.bookyourstay.modelo.Cliente;
import co.edu.uniquindio.bookyourstay.modelo.Factura;
import co.edu.uniquindio.bookyourstay.modelo.Reserva;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
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
public class ReservaAlojamientosControlador implements Observable, Initializable {

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


    public ReservaAlojamientosControlador() {
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

    @FXML
    public void realizarPago() {
        try {
            // Validar fechas
            LocalDate fechaInicio = obtenerFechaInicio();
            LocalDate fechaFin = obtenerFechaFin();
            if (fechaInicio == null || fechaFin == null || !fechaFin.isAfter(fechaInicio)) {
                principalControlador.mostrarAlerta("Las fechas seleccionadas no son válidas.", Alert.AlertType.ERROR);
                return;
            }

            int numHuespedes = obtenerNumeroHuespedes();
            if (numHuespedes == -1) return;

            Cliente cliente = principalControlador.getSesion().getCliente();
            if (cliente == null) {
                principalControlador.mostrarAlerta("No se encontró un cliente en sesión.", Alert.AlertType.ERROR);
                return;
            }

            Alojamiento alojamiento = principalControlador.getSesion().getReservarAlojamiento();
            if (alojamiento == null) {
                principalControlador.mostrarAlerta("No se encontró un alojamiento seleccionado.", Alert.AlertType.ERROR);
                return;
            }
            Reserva reserva = principalControlador.realizarReserva(cliente, alojamiento, fechaInicio, fechaFin, numHuespedes, null);
            if (reserva == null) {
                principalControlador.mostrarAlerta("No se pudo realizar la reserva. Faltan datos o parámetros inválidos.", Alert.AlertType.ERROR);
                return;
            }
            Factura factura = principalControlador.generarFactura(reserva);
            if (factura == null) {
                principalControlador.mostrarAlerta("No se pudo generar la factura.", Alert.AlertType.ERROR);
                return;
            }
            String saldoResultado = principalControlador.verificarSaldoDisponible(cliente, factura.getTotal());
            if (!saldoResultado.startsWith("Saldo suficiente")) {
                principalControlador.mostrarAlerta(saldoResultado, Alert.AlertType.WARNING);
                return;
            }
            cliente.getBilleteraVirtual().setMontoTotal(cliente.getBilleteraVirtual().getMontoTotal() - factura.getTotal());
            principalControlador.getSesion().setCliente(cliente);

            principalControlador.mostrarAlerta("Reserva realizada con éxito. El comprobante ha sido enviado al correo.", Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            principalControlador.mostrarAlerta("Ocurrió un error al procesar la reserva: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @Override
    public void notificar() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        obtenerAlojamiento();
        int capacidadMaxima = alojamiento.getCapacidadMaxima();
        cbNumHuespedes.setItems(FXCollections.observableArrayList(
                IntStream.rangeClosed(1, capacidadMaxima)
                        .boxed()
                        .collect(Collectors.toList())
        ));

        cbNumHuespedes.getSelectionModel().selectFirst();
    }

    public void irInicio() {
        principalControlador.navegarVentana("/panelUsuario.fxml", "Panel usuario");
        principalControlador.cerrarVentana(btnRegresar);
    }
}
