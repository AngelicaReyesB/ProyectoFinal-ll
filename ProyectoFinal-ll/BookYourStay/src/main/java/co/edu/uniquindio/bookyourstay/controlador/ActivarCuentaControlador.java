package co.edu.uniquindio.bookyourstay.controlador;

import co.edu.uniquindio.bookyourstay.controlador.observador.Observable;
import co.edu.uniquindio.bookyourstay.modelo.Alojamiento;
import co.edu.uniquindio.bookyourstay.modelo.Cliente;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import lombok.Setter;

import java.net.URL;
import java.util.ResourceBundle;

public class ActivarCuentaControlador implements Observable, Initializable {

    @FXML private TextField codigoActivacion;
    private final PrincipalControlador principalControlador;

    // Asegúrate de que observable sea inyectado correctamente
    @Setter
    private Observable observable;

    public ActivarCuentaControlador() {
        principalControlador = PrincipalControlador.getInstancia();
    }

    @FXML
    public void activarCuenta() {
        try {
            Cliente cliente = principalControlador.getSesion().getCliente();

            // Verificar si la cuenta ya está activada
            if (cliente.isEstadoCuenta()) {
                principalControlador.mostrarAlerta("La cuenta ya está activada", Alert.AlertType.INFORMATION);
                return;  // Si ya está activada, no hacer nada más
            }

            // Intentar activar la cuenta
            boolean cuentaActivada = principalControlador.activarUsuario(codigoActivacion.getText(), cliente);
            System.out.println("cuenta activada: " + cuentaActivada);

            if (cuentaActivada) {
                // Actualizar el cliente en la sesión después de la activación
                principalControlador.getSesion().setCliente(cliente);
                if (observable != null) {
                    observable.notificar(); // Solo llamar a notificar si observable no es nulo
                }

                // Mostrar mensaje de éxito y cerrar ventana
                principalControlador.mostrarAlerta("Cuenta activada con éxito", Alert.AlertType.INFORMATION);
                principalControlador.navegarVentana("/panelUsuario.fxml", "Panel del usuario");
                principalControlador.cerrarVentana(codigoActivacion);
                navegar();  // Navegar a otra vista si es necesario
            } else {
                // Si no se pudo activar la cuenta
                principalControlador.mostrarAlerta("Los datos de usuario y código no coinciden. Intenta nuevamente", Alert.AlertType.WARNING);
            }

        } catch (Exception e) {
            // Mostrar mensaje de error si ocurre alguna excepción
            principalControlador.mostrarAlerta("Hubo un error al activar la cuenta: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();  // Imprimir en consola el error para debugging
        }
    }

    private void navegar() {
        Alojamiento reservaAlojamiento = principalControlador.getSesion().getReservarAlojamiento();
        Cliente cliente = principalControlador.getSesion().getCliente();
        if (reservaAlojamiento != null) {
            // Si el cliente tiene una reserva, navegar a la ventana de reserva de alojamiento
            principalControlador.navegarVentana("/reservaAlojamiento.fxml", "Reserva alojamiento.");
            principalControlador.cerrarVentana(codigoActivacion);
        }
        if (cliente != null && reservaAlojamiento == null) {
            // Si el cliente no tiene una reserva, navegar al panel de usuario
            principalControlador.navegarVentana("/panelUsuario.fxml", "Panel usuario.");
            principalControlador.cerrarVentana(codigoActivacion);
        }
    }

    @Override
    public void notificar() {
        // Implementación de notificación si es necesario
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicialización si es necesario
    }
}
