package co.edu.uniquindio.bookyourstay.controlador;

import co.edu.uniquindio.bookyourstay.controlador.observador.Observable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

//hecho
public class BarraClienteControlador implements Observable, Initializable {

    @FXML private Button btnIrPerfil;
    private final PrincipalControlador principalControlador;

    public BarraClienteControlador(){
        principalControlador = PrincipalControlador.getInstancia();
    }

    @FXML
    public void irPerfil() {
        try {
            principalControlador.navegarVentana("/perfil.fxml", "Perfil Usuario");
            principalControlador.cerrarVentana(btnIrPerfil);
        }catch (Exception e){
            principalControlador.mostrarAlerta(e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @Override
    public void notificar() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
