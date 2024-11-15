package co.edu.uniquindio.bookyourstay.controlador;

import co.edu.uniquindio.bookyourstay.controlador.observador.Observable;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class PerfilControlador implements Observable, Initializable {

    @FXML private Button btnActualizarDatos;
    @FXML private Button btnBilleteraVirtual;
    @FXML private Button btnCancelarReserva;
    @FXML private Button btnEliminarCuenta;
    @FXML private Button btnRegresar;
    @FXML private Button btnVerDetalles;
    @FXML private ListView<?> listaReservas;



    @FXML void detallesAlojamiento(ActionEvent event) {

    }

    @FXML
    void eliminarCuenta(ActionEvent event) {

    }

    @FXML
    void irActualizarDatosCliente(ActionEvent event) {

    }

    @FXML
    void irBilleteraVirtual(ActionEvent event) {

    }

    @FXML
    void irPanelUsuario(ActionEvent event) {

    }

    @Override
    public void notificar() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
