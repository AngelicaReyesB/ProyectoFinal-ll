package co.edu.uniquindio.bookyourstay.controlador;

import co.edu.uniquindio.bookyourstay.controlador.observador.Observable;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

//método de verificación del código para cambiar contraseña
public class NuevaPasswordControlador implements Observable, Initializable {

    @FXML private Button btnRestablecerPassword;
    @FXML private TextField codigo;
    @FXML private Label mensajeLabel;
    @FXML private TextField nuevaPassword;

    @FXML
    public void restablecerPassword() {

    }

    @Override
    public void notificar() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
