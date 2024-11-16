package co.edu.uniquindio.bookyourstay.controlador;

import co.edu.uniquindio.bookyourstay.controlador.observador.Observable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

//funciona
public class BarraNavegacionControlador implements Observable, Initializable {

    @FXML
    private Button btnRegistrarse;
    private final PrincipalControlador principalControlador;

    public BarraNavegacionControlador(){
        principalControlador = PrincipalControlador.getInstancia();
    }

    @FXML
    public void registrarUsuario() {
        principalControlador.navegarVentana("/registroCliente.fxml", "Panel para registro.");
        principalControlador.cerrarVentana(btnRegistrarse);
    }

    @Override
    public void notificar() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
