package co.edu.uniquindio.bookyourstay.controlador;

import co.edu.uniquindio.bookyourstay.controlador.observador.Observable;
import co.edu.uniquindio.bookyourstay.modelo.Alojamiento;
import co.edu.uniquindio.bookyourstay.modelo.Cliente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ActivarCuentaControlador implements Observable, Initializable {

    @FXML private TextField codigoActivacion;
    private final PrincipalControlador principalControlador;
    private Observable observable;

    public ActivarCuentaControlador() {
        principalControlador = PrincipalControlador.getInstancia();
    }

    @FXML
    void activarCuenta(ActionEvent event) {
        try {
            Cliente cliente = principalControlador.getSesion().getCliente();
            System.out.println("CLIENTE PARA ACTUALIZAR: " + cliente);
            boolean cuentaActivada = principalControlador.activarUsuario(codigoActivacion.getText(), cliente);
            System.out.println("cuenta activada: " + cuentaActivada);
            if (cuentaActivada) {
                System.out.println(principalControlador.obtenerCliente(cliente.getCedula()));
                principalControlador.getSesion().setCliente(principalControlador.obtenerCliente(cliente.getCedula()));
                observable.notificar();
                principalControlador.mostrarAlerta("Cuenta activada con éxito", Alert.AlertType.INFORMATION);
                principalControlador.cerrarVentana(codigoActivacion);
                navegar();
            }else {
                principalControlador.mostrarAlerta("Los datos de usuario y  código no coinciden. Intenta nuevamente", Alert.AlertType.WARNING);
            }

        }catch (Exception e) {

        }
    }

    private void navegar() {
        Alojamiento reservaAlojamiento = principalControlador.getSesion().getReservarAlojamiento();
        Cliente cliente = principalControlador.getSesion().getCliente();
        if(reservaAlojamiento != null){
            principalControlador.navegarVentana("/reservaAlojamiento.fxml", "Reserva alojamiento.");
            principalControlador.cerrarVentana(codigoActivacion);
        }
        if(cliente != null && reservaAlojamiento == null){
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
