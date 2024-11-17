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

//funciona correctamente
public class ActivarCuentaControlador implements Observable, Initializable {

    @FXML private TextField codigoActivacion;
    private final PrincipalControlador principalControlador;
    @Setter
    private Observable observable;

    public ActivarCuentaControlador() {
        principalControlador = PrincipalControlador.getInstancia();
    }

    @FXML
    public void activarCuenta() {
        try {
            Cliente cliente = principalControlador.getSesion().getCliente(); // Obtener cliente de la sesión

            // Verifica si la cuenta ya está activada
            if (cliente.isEstadoCuenta()) {
                principalControlador.mostrarAlerta("La cuenta ya está activada", Alert.AlertType.INFORMATION);
                return;
            }

            // Verificar si el código ingresado es el correcto
            boolean cuentaActivada = principalControlador.activarUsuario(codigoActivacion.getText(), cliente);

            if (cuentaActivada) {
                // Si la cuenta se activa, actualiza el estado en la sesión y en la vista
                cliente.setEstadoCuenta(true);  // Activar la cuenta
                principalControlador.getSesion().setCliente(cliente);  // Actualiza el cliente en la sesión

                // Mostrar mensaje de éxito
                principalControlador.mostrarAlerta("Cuenta activada con éxito", Alert.AlertType.INFORMATION);

                // Navegar a la vista del usuario
                principalControlador.navegarVentana("/inicio.fxml", "Panel del usuario");
                principalControlador.cerrarVentana(codigoActivacion);
                navegar();
            } else {
                principalControlador.mostrarAlerta("Los datos de usuario y código no coinciden. Intenta nuevamente", Alert.AlertType.WARNING);
            }
        } catch (Exception e) {
            principalControlador.mostrarAlerta("Hubo un error al activar la cuenta: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }


    private void navegar() {
        Alojamiento reservaAlojamiento = principalControlador.getSesion().getReservarAlojamiento();
        Cliente cliente = principalControlador.getSesion().getCliente();
        if (reservaAlojamiento != null) {
            principalControlador.navegarVentana("/reservaAlojamiento.fxml", "Reserva alojamiento.");
            principalControlador.cerrarVentana(codigoActivacion);
        }
        if (cliente != null && reservaAlojamiento == null) {
            principalControlador.navegarVentana("/panelUsuario.fxml", "Panel usuario.");
            principalControlador.cerrarVentana(codigoActivacion);
        }
    }

    @Override
    public void notificar() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
