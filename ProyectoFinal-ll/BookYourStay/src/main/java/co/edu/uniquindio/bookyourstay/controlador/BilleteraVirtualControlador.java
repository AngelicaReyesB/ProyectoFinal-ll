package co.edu.uniquindio.bookyourstay.controlador;

import co.edu.uniquindio.bookyourstay.controlador.observador.Observable;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class BilleteraVirtualControlador implements Observable, Initializable {

    @FXML
    private Label labelSaldoDisponible;

    @FXML
    private TextField codigoActivacion;

    @FXML
    private Button btnRecargar;

    private double saldo = 0.0;

    @FXML
    private void recargarBilletera(ActionEvent event) {
        try {
            double montoRecarga = Double.parseDouble(codigoActivacion.getText());

            if (montoRecarga > 0) {
                saldo += montoRecarga;
                labelSaldoDisponible.setText(String.format("Saldo disponible: $%.2f", saldo));
                codigoActivacion.clear();
                System.out.println("Recarga realizada con éxito. Nuevo saldo: $" + saldo);
            } else {
                System.out.println("Por favor, ingrese un monto válido.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Ingrese un valor numérico para la recarga.");
        }
    }

    @FXML
    private void initialize() {
        actualizarSaldoEnPantalla();
    }

    private void actualizarSaldoEnPantalla() {
        labelSaldoDisponible.setText(String.format("Saldo disponible: $%.2f", saldo));
    }

    @Override
    public void notificar() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
