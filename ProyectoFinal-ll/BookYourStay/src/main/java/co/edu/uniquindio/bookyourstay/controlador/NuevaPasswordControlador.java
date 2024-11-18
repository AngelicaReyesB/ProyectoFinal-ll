package co.edu.uniquindio.bookyourstay.controlador;

import co.edu.uniquindio.bookyourstay.controlador.observador.Observable;
import co.edu.uniquindio.bookyourstay.modelo.Administrador;
import co.edu.uniquindio.bookyourstay.modelo.Cliente;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

//hecho
public class NuevaPasswordControlador implements Observable, Initializable {

    @FXML private TextField codigoField;
    @FXML private Label mensajeLabel;
    @FXML private PasswordField nuevaPasswordField;
    private final PrincipalControlador principalControlador;

    public NuevaPasswordControlador() {
        principalControlador = PrincipalControlador.getInstancia();
    }

    @FXML
    public void restablecerPassword() {
        try {
            String nuevaPassword = nuevaPasswordField.getText();
            String codigoActivacion = codigoField.getText();

            if (principalControlador.getSesion().isAdministrador()) {
                String emailAdmin = "admin@bookyourstay.com";
                Administrador administrador = principalControlador.cambiarPassword(emailAdmin, nuevaPassword);
                mensajeLabel.setText("Contraseña del administrador actualizada exitosamente.");
            } else {
                String cedulaCliente = principalControlador.getSesion().getCliente().getCedula();
                Cliente cliente = principalControlador.cambiarPasswordC(cedulaCliente, nuevaPassword, codigoActivacion);
                mensajeLabel.setText("Contraseña del cliente actualizada exitosamente.");
            }
        } catch (Exception e) {
            mensajeLabel.setText(e.getMessage());
        }
    }

    @Override
    public void notificar() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mensajeLabel.setText("");
    }
}
