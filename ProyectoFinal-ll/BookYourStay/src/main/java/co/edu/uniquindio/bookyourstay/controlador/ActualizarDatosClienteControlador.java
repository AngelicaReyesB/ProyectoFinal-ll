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
    public void irPerfil() {
        try {
            principalControlador.navegarVentana("/perfil.fxml", "Perfil");
            principalControlador.cerrarVentana(btnRegresar);
        } catch (Exception e) {
            principalControlador.mostrarAlerta(e.getMessage(), AlertType.ERROR);
        }
    }

    @FXML
    public void actualizarDatos() {
        if (cliente != null) {
            if (camposValidos()) {
                try {
                    cliente.setNombre(nombre.getText());
                    cliente.setCedula(cedula.getText());
                    cliente.setTelefono(telefono.getText());
                    cliente.setEmail(correo.getText());
                    cliente.setPassword(password.getText());
                    principalControlador.editarCuenta(cliente.getCedula(), cliente.getNombre(),
                            cliente.getTelefono(), cliente.getEmail(),
                            cliente.getPassword());
                    principalControlador.mostrarAlerta("Cuenta actualizada correctamente", AlertType.CONFIRMATION);
                    limpiarCampos();
                    principalControlador.navegarVentana("/inicio.fxml", "Inicio");
                    principalControlador.cerrarVentana(btnGuardarCambios);
                } catch (Exception e) {
                    principalControlador.mostrarAlerta("Error al actualizar la cuenta: " + e.getMessage(), AlertType.ERROR);
                    e.printStackTrace();
                }
            } else {
                principalControlador.mostrarAlerta("Por favor, completa todos los campos antes de guardar.", AlertType.WARNING);
            }
        } else {
            principalControlador.mostrarAlerta("No se encontró información del cliente.", AlertType.ERROR);
        }
    }

    private boolean camposValidos() {
        return !nombre.getText().isBlank() && !cedula.getText().isBlank() &&
                !telefono.getText().isBlank() && !correo.getText().isBlank() &&
                !password.getText().isBlank();
    }




    public void limpiarCampos(){
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
        if (cliente != null) {
            nombre.setText(cliente.getNombre());
            cedula.setText(cliente.getCedula());
            telefono.setText(cliente.getTelefono());
            correo.setText(cliente.getEmail());
            password.setText(cliente.getPassword());
        }
    }


}

