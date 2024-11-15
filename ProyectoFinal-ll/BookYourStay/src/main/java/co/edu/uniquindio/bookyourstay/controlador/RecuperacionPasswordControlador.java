package co.edu.uniquindio.bookyourstay.controlador;

import co.edu.uniquindio.bookyourstay.controlador.observador.Observable;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class RecuperacionPasswordControlador implements Observable, Initializable {

    @FXML
    private TextField codigoActivacion; // Campo de correo electrónico

    @FXML
    private Button btnEnviarCodigo;

    @FXML
    private Label mensajeEstado;

    @FXML
    private void recuperacionContraseña(ActionEvent event) {
        String correo = codigoActivacion.getText();

        if (correo == null || correo.trim().isEmpty()) {
            mostrarMensajeEstado("Por favor, ingrese su correo.", "rojo");
            return;
        }

        if (!correoValido(correo)) {
            mostrarMensajeEstado("El correo ingresado no es válido.", "rojo");
            return;
        }

        boolean enviado = enviarCodigoRecuperacion(correo);

        if (enviado) {
            mostrarMensajeEstado("El código de recuperación ha sido enviado.", "verde");
        } else {
            mostrarMensajeEstado("Hubo un problema al enviar el código. Intente de nuevo.", "rojo");
        }
    }

    private boolean correoValido(String correo) {
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return correo.matches(regex);
    }

    private boolean enviarCodigoRecuperacion(String correo) {
        System.out.println("Enviando código de recuperación al correo: " + correo);
        return true;
    }

    private void mostrarMensajeEstado(String mensaje, String color) {
        if (mensajeEstado == null) {
            mensajeEstado = new Label(); // Asegura que el Label existe
        }
        mensajeEstado.setText(mensaje);
        mensajeEstado.setStyle("-fx-text-fill: " + color + ";");
    }

    @Override
    public void notificar() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
