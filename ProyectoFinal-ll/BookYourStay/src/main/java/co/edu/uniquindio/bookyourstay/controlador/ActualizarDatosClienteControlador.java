package co.edu.uniquindio.bookyourstay.controlador;

import co.edu.uniquindio.bookyourstay.controlador.observador.Observable;
import co.edu.uniquindio.bookyourstay.modelo.Cliente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;


//no funciona
public class ActualizarDatosClienteControlador implements Observable, Initializable {

    @FXML private Button btnRegresar;
    @FXML private Button btnGuardarCambios;
    @FXML private TextField nombre;
    @FXML private TextField cedula;
    @FXML private TextField telefono;
    @FXML private TextField correo;
    @FXML private TextField password;
    private final PrincipalControlador principalControlador;
    private Cliente cliente;

    public ActualizarDatosClienteControlador(){
        principalControlador = PrincipalControlador.getInstancia();
    }

    @FXML
    private void irPerfil() {
        try {
            principalControlador.navegarVentana("/perfil.fxml", "Perfil");
            principalControlador.cerrarVentana(btnRegresar);
        } catch (Exception e) {
            principalControlador.mostrarAlerta(e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    private void actualizarDatos() {
        if (cliente != null) {
            try {
                principalControlador.editarCuenta(cedula.getText(), nombre.getText(), telefono.getText(), correo.getText(), password.getText());
                limpiarCampos();
                principalControlador.mostrarAlerta("Cuenta actualizada correctamente", AlertType.CONFIRMATION);
                principalControlador.navegarVentana("/inicio.fxml", "Inicio");
            } catch (Exception e) {
                principalControlador.mostrarAlerta("Error al actualizar la cuenta", AlertType.ERROR);
                e.printStackTrace();
            }
        }
    }


    private void limpiarCampos(){
        nombre.clear();
        cedula.clear();
        telefono.clear();
        correo.clear();
        password.clear();
    }

    @Override
    public void notificar() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

}

