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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter

public class NuevaPasswordControlador implements Observable, Initializable {

    @FXML
    private TextField codigoField;

    @FXML
    private TextField nuevaContraseñaField;

    @FXML
    private Button btnRestablecerContraseña;

    @FXML
    private Label mensajeLabel;

    private String codigoVerificacionCorrecto;

    @FXML
    private void restablecerContraseña(ActionEvent event) {
        String codigoIngresado = codigoField.getText();
        String nuevaContraseña = nuevaContraseñaField.getText();

        if (codigoIngresado == null || codigoIngresado.isEmpty()) {
            mostrarMensaje("Por favor, ingrese el código de verificación.", "rojo");
            return;
        }

        if (!codigoIngresado.equals(codigoVerificacionCorrecto)) {
            mostrarMensaje("El código de verificación no es válido.", "rojo");
            return;
        }

        if (nuevaContraseña == null || nuevaContraseña.isEmpty()) {
            mostrarMensaje("Por favor, ingrese su nueva contraseña.", "rojo");
            return;
        }

        if (nuevaContraseña.length() < 6) {
            mostrarMensaje("La contraseña debe tener al menos 6 caracteres.", "rojo");
            return;
        }

        actualizarContraseña(nuevaContraseña);
        mostrarMensaje("Contraseña restablecida con éxito.", "verde");
        limpiarCampos();
    }

    private void mostrarMensaje(String mensaje, String color) {
        mensajeLabel.setText(mensaje);
        mensajeLabel.setStyle("-fx-text-fill: " + color + ";");
    }

    private void actualizarContraseña(String nuevaContraseña) {
        System.out.println("Contraseña actualizada: " + nuevaContraseña);
    }

    private void limpiarCampos() {
        codigoField.clear();
        nuevaContraseñaField.clear();
    }

    @Override
    public void notificar() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
