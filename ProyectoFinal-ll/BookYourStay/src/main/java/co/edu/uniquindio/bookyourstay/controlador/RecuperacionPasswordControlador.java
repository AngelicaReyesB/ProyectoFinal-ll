package co.edu.uniquindio.bookyourstay.controlador;

import co.edu.uniquindio.bookyourstay.controlador.observador.Observable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class RecuperacionPasswordControlador implements Observable, Initializable {

    @FXML private Button btnEnviarCodigo;
    @FXML private TextField correo;
    private final PrincipalControlador principalControlador;

    public RecuperacionPasswordControlador(){
        principalControlador = PrincipalControlador.getInstancia();
    }

    @FXML
    public void recuperacionPassword( ) {
        principalControlador.navegarVentana("/nuevaPassword.fxml", "Nueva contraseña.");
        principalControlador.cerrarVentana(btnEnviarCodigo);
    }

    @Override
    public void notificar() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
