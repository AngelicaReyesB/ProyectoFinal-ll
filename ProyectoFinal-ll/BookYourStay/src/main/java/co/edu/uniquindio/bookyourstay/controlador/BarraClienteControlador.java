package co.edu.uniquindio.bookyourstay.controlador;

import co.edu.uniquindio.bookyourstay.controlador.observador.Observable;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class BarraClienteControlador implements Observable, Initializable {

    @FXML
    private Button btnIrPerfil;

    // Método para manejar el evento del botón "Perfil"
    @FXML
    public void irPerfil(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/perfil.fxml"));
            Parent perfilRoot = loader.load();

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene perfilScene = new Scene(perfilRoot);

            currentStage.setScene(perfilScene);
            currentStage.show();

            System.out.println("Navegando al perfil del usuario");

        } catch (IOException e) {
            System.err.println("Error al cargar la vista del perfil: " + e.getMessage());
        }
    }

    @Override
    public void notificar() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
