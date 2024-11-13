package co.edu.uniquindio.bookyourstay.controlador;

import co.edu.uniquindio.bookyourstay.controlador.observador.Observable;
import co.edu.uniquindio.bookyourstay.modelo.Cliente;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class RegistroClienteControlador implements Observable, Initializable {

    @FXML private TextField cedula;
    @FXML private TextField correo;
    @FXML private TextField nombre;
    @FXML private TextField password;
    @FXML private TextField telefono;
    private final PrincipalControlador principalControlador;
    private Observable observable;

    public RegistroClienteControlador() {
        principalControlador = PrincipalControlador.getInstancia();
    }

    @FXML void irPanelInicio(ActionEvent event) {
        principalControlador.navegarVentana("/inicio.fxml", "Inicio");
        principalControlador.cerrarVentana(nombre);
    }

    @FXML
    void registrarUsuario(ActionEvent event) {
        if(nombre.getText().isEmpty() || cedula.getText().isEmpty() || telefono.getText().isEmpty() ||
                correo.getText().isEmpty() || password.getText().isEmpty()) {
            principalControlador.mostrarAlerta("Todos los campos son obligatorios para el registro",
                    Alert.AlertType.WARNING);
        } else {
            try {
                System.out.println("CEDULA PARA BUSCAR" + cedula.getText());
                Cliente cliente = principalControlador.registrarCliente(
                        nombre.getText(), cedula.getText(), telefono.getText(), correo.getText(), password.getText());
                if(cliente != null) {
                    principalControlador.mostrarAlerta(
                            "El usuario fue creado con exito. \n\nIngresa y activa la cuenta con el código enviado",
                            Alert.AlertType.INFORMATION
                    );
                    observable.notificar();
                    principalControlador.navegarVentana("/inicio.fxml", "Inicio.");
                    principalControlador.getSesion().setCliente(null);
                    principalControlador.cerrarVentana(nombre);
                }else {
                    principalControlador.mostrarAlerta("No fue posible crear al usuario", Alert.AlertType.WARNING);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                principalControlador.mostrarAlerta(e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @Override
    public void notificar() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nombre.clear();
        cedula.clear();
        telefono.clear();
        correo.clear();
        password.clear();
    }
}
