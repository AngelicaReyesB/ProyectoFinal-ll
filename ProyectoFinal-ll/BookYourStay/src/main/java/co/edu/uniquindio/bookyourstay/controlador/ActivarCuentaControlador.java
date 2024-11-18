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
    @Setter
    private Observable observable;

    public ActivarCuentaControlador() {
        principalControlador = PrincipalControlador.getInstancia();
    }

    @FXML
    public void activarCuenta() {
        try {
            Cliente cliente = principalControlador.getSesion().getCliente();
            if (cliente.isEstadoCuenta()) {
                principalControlador.mostrarAlerta("La cuenta ya está activada", Alert.AlertType.INFORMATION);
                return;
            }
            boolean cuentaActivada = principalControlador.activarUsuario(codigoActivacion.getText(), cliente);
            if (cuentaActivada) {
                cliente.setEstadoCuenta(true);
                principalControlador.getSesion().setCliente(cliente);
                principalControlador.mostrarAlerta("Cuenta activada con éxito", Alert.AlertType.INFORMATION);
                principalControlador.navegarVentana("/inicio.fxml", "Inicie Sesión");
                principalControlador.cerrarVentana(codigoActivacion);
            } else {
                principalControlador.mostrarAlerta("Los datos de usuario y código no coinciden. Intenta nuevamente", Alert.AlertType.WARNING);
            }
        } catch (Exception e) {
            principalControlador.mostrarAlerta("Hubo un error al activar la cuenta: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @Override
    public void notificar() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
