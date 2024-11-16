package co.edu.uniquindio.bookyourstay.controlador;

import co.edu.uniquindio.bookyourstay.controlador.observador.Observable;
import co.edu.uniquindio.bookyourstay.utils.EnvioEmail;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;
import java.util.ResourceBundle;

//hecho
public class RecuperacionPasswordControlador implements Observable, Initializable {

    @FXML private Button btnEnviarCodigo;
    @FXML private TextField correo;
    @FXML private Label mensajeLabel;

    private final PrincipalControlador principalControlador;

    public RecuperacionPasswordControlador() {
        principalControlador = PrincipalControlador.getInstancia();
    }

    @FXML
    void recuperacionPassword() {
        String emailIngresado = correo.getText();

        try {
            if (emailIngresado == null || emailIngresado.isBlank()) {
                mensajeLabel.setText("Por favor, ingresa un correo válido.");
                return;
            }
            if (emailIngresado.equals("admin@bookyourstay.com")) {
                principalControlador.obtenerAdministrador(emailIngresado);
                mensajeLabel.setText("Código de recuperación enviado al administrador.");
            } else {
                principalControlador.obtenerUsuario(emailIngresado);
                mensajeLabel.setText("Código de recuperación enviado al correo ingresado.");
            }
            principalControlador.navegarVentana("/nuevaPassword.fxml", "Panel para ingresar nueva contraseña");
            principalControlador.cerrarVentana(btnEnviarCodigo);
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
        if (mensajeLabel != null) {
            mensajeLabel.setText("Ingresa tu correo para recibir el código.");
        } else {
            System.out.println("mensajeLabel no está inicializado.");
        }
    }
}
