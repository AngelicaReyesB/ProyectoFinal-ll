package co.edu.uniquindio.bookyourstay.controlador;

import co.edu.uniquindio.bookyourstay.controlador.observador.Observable;
import co.edu.uniquindio.bookyourstay.modelo.Cliente;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

//hecho, no modificar
public class BilleteraVirtualControlador implements Observable, Initializable {

    @FXML private Label labelSaldoDisponible;
    @FXML private Button btnRecargar;
    @FXML private Button btnRegresarPanel;
    @FXML private TextField txtMontoRecarga;
    private double saldo = 0.0;
    private final PrincipalControlador principalControlador;

    public BilleteraVirtualControlador(){
        principalControlador = PrincipalControlador.getInstancia();
    }

    @FXML
    private void recargarBilletera() {
        try {
            double montoRecarga = Double.parseDouble(txtMontoRecarga.getText());

            if (montoRecarga > 0) {
                Cliente clienteActual = principalControlador.getSesion().getCliente();
                principalControlador.recargarBilleteraVirtual(clienteActual, (float) montoRecarga);
                saldo = clienteActual.getBilleteraVirtual().getMontoTotal();
                principalControlador.mostrarAlerta("Recarga realizada con éxito. Nuevo saldo: $" + saldo, Alert.AlertType.CONFIRMATION);
            } else {
                principalControlador.mostrarAlerta("Por favor, ingrese un monto válido.", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            principalControlador.mostrarAlerta("Error: Ingrese un valor numérico para la recarga.", Alert.AlertType.ERROR);
        } catch (Exception e) {
            principalControlador.mostrarAlerta("Error en la recarga: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void regresarPanel(ActionEvent event) {
        try {
            principalControlador.navegarVentana("/reservaAlojamiento.fxml", "Panel de reservas");
            principalControlador.cerrarVentana(btnRegresarPanel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void actualizarSaldoEnPantalla() {
        labelSaldoDisponible.setText(String.format("Saldo disponible: $%.2f", saldo));
    }

    @Override
    public void notificar() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        actualizarSaldoEnPantalla();
    }
}