package co.edu.uniquindio.bookyourstay.controlador;

import co.edu.uniquindio.bookyourstay.controlador.observador.Observable;
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

@Setter
@Getter

public class ActualizarDatosClienteControlador implements Observable, Initializable {

    @FXML
    private Button btnRegresar;

    @Setter
    @FXML
    private Button btnGuardarCambios;

    @FXML
    private TextField nombre;

    @FXML
    private TextField cedula;

    @FXML
    private TextField telefono;

    @FXML
    private TextField correo;

    @FXML
    private TextField password;

    @FXML
    private void irPerfil(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/perfil.fxml"));
            Parent perfilRoot = loader.load();

            Stage perfilStage = new Stage();
            perfilStage.setTitle("Perfil de Usuario");

            perfilStage.setScene(new Scene(perfilRoot));

            perfilStage.show();

            System.out.println("Ventana de perfil abierta en una nueva ventana");

        } catch (IOException e) {
            System.err.println("Error al abrir la ventana de perfil: " + e.getMessage());
        }
    }

    @FXML
    private void ActualizarDatos(ActionEvent event) {
        String nombreCliente = nombre.getText();
        String cedulaCliente = cedula.getText();
        String telefonoCliente = telefono.getText();
        String correoCliente = correo.getText();
        String passwordCliente = password.getText();

        if (nombreCliente.isEmpty() || cedulaCliente.isEmpty() || telefonoCliente.isEmpty() || correoCliente.isEmpty() || passwordCliente.isEmpty()) {
            mostrarAlerta("Campos incompletos", "Por favor, complete todos los campos antes de guardar los cambios.");
            return;
        }

        System.out.println("Datos actualizados correctamente:");
        System.out.println("Nombre: " + nombreCliente);
        System.out.println("Cédula: " + cedulaCliente);
        System.out.println("Teléfono: " + telefonoCliente);
        System.out.println("Correo: " + correoCliente);
        System.out.println("Contraseña: " + passwordCliente);

        mostrarAlerta("Éxito", "Los datos del cliente se han actualizado correctamente.");
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    @Override
    public void notificar() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

}

