package co.edu.uniquindio.bookyourstay.controlador;

import co.edu.uniquindio.bookyourstay.controlador.observador.Observable;
import co.edu.uniquindio.bookyourstay.modelo.Cliente;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.Setter;

import java.net.URL;
import java.util.ResourceBundle;

//funciona correctamente
public class RegistroClienteControlador implements Observable, Initializable {

    @FXML private TextField cedula;
    @FXML private TextField correo;
    @FXML private TextField nombre;
    @FXML private PasswordField password;
    @FXML private TextField telefono;
    @FXML private Hyperlink iniciaSesionHyperlink;
    private PrincipalControlador principalControlador;
    @Setter
    private Observable observable;

    public RegistroClienteControlador() {
    }

    @FXML
    public void irPanelInicio() {
        principalControlador.navegarVentana("/inicio.fxml", "Inicio");
        principalControlador.cerrarVentana(nombre);
    }

    //cuando se haga el nuevo gmail, puedo cambiar el método
    @FXML
    public void registrarUsuario() {
        if (nombre.getText().isEmpty() || cedula.getText().isEmpty() || telefono.getText().isEmpty() ||
                correo.getText().isEmpty() || password.getText().isEmpty()) {
            principalControlador.mostrarAlerta("Todos los campos son obligatorios para el registro",
                    Alert.AlertType.WARNING);
        } else {
            try {
                System.out.println("CÉDULA PARA BUSCAR" + cedula.getText());
                // Registrar cliente
                Cliente cliente = principalControlador.registrarCliente(
                        nombre.getText(), cedula.getText(), telefono.getText(), correo.getText(), password.getText());
                if (cliente != null) {
                    principalControlador.getSesion().setCliente(cliente);
                    principalControlador.enviarCodigoActivacion(cliente);  // Llamada al método para enviar el correo
                    principalControlador.mostrarAlerta(
                            "El usuario fue creado con éxito. \n\nIngresa y activa la cuenta con el código enviado",
                            Alert.AlertType.INFORMATION
                    );
                    if (observable != null) {
                        observable.notificar();
                    }
                    principalControlador.navegarVentana("/activarCuenta.fxml", "Activación de la cuenta.");
                    principalControlador.cerrarVentana(nombre);
                } else {
                    principalControlador.mostrarAlerta("No fue posible crear al usuario", Alert.AlertType.WARNING);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                principalControlador.mostrarAlerta(e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    public void inicioSesion(){
        principalControlador.navegarVentana("/inicio.fxml", "Inicio");
        principalControlador.cerrarVentana(iniciaSesionHyperlink);
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
        principalControlador = PrincipalControlador.getInstancia();
    }
}