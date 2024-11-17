package co.edu.uniquindio.bookyourstay.controlador;

import co.edu.uniquindio.bookyourstay.controlador.observador.Observable;
import co.edu.uniquindio.bookyourstay.modelo.Alojamiento;
import co.edu.uniquindio.bookyourstay.modelo.Cliente;
import co.edu.uniquindio.bookyourstay.modelo.Reserva;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

//probar para ver si funciona
public class PerfilControlador implements Observable, Initializable {

    @FXML private Button btnActualizarDatos;
    @FXML private Button btnBilleteraVirtual;
    @FXML private Button btnEliminarCuenta;
    @FXML private Button btnRegresar;
    @FXML private Button btnVerDetalles;
    @FXML private ListView<Reserva> listaReservas;
    private final PrincipalControlador principalControlador;
    public PerfilControlador(){
        principalControlador = PrincipalControlador.getInstancia();
    }

    @FXML
    public void cancelarReserva() {
        Reserva reservaSeleccionada = listaReservas.getSelectionModel().getSelectedItem();
        if (reservaSeleccionada == null) {
            principalControlador.mostrarAlerta("Por favor, seleccione una reserva para cancelar.", Alert.AlertType.WARNING);
            return;
        }
        try {
            boolean cancelada = principalControlador.cancelarReserva(reservaSeleccionada);
            if (cancelada) {
                principalControlador.mostrarAlerta("Reserva cancelada con éxito.", Alert.AlertType.CONFIRMATION);
                listaReservas.getItems().remove(reservaSeleccionada);
            }
        } catch (Exception e) {
            principalControlador.mostrarAlerta("Error al cancelar la reserva: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void detallesAlojamiento() {
        principalControlador.navegarVentana("/detallesAlojamiento.fxml", "Detalles de los alojamientos");
        principalControlador.cerrarVentana(btnVerDetalles);
    }

    @FXML
    public void eliminarCuenta(ActionEvent event) {
        Cliente clienteActual = principalControlador.getSesion().getCliente();
        if (clienteActual != null) {
            String cedulaCliente = clienteActual.getCedula();

            try {
                boolean eliminado = principalControlador.eliminarCuentaCliente(cedulaCliente);

                if (eliminado) {
                    principalControlador.mostrarAlerta("Cliente eliminado con éxito", Alert.AlertType.CONFIRMATION);
                    principalControlador.navegarVentana("/inicio.fxml", "Inicio");
                    principalControlador.cerrarVentana(btnEliminarCuenta);
                }
            } catch (Exception e) {
                principalControlador.mostrarAlerta("No se pudo eliminar la cuenta: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            principalControlador.mostrarAlerta("No hay ningún cliente logueado", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void irActualizarDatosCliente(ActionEvent event) {
        principalControlador.navegarVentana("/actualizarDatosCliente.fxml", "Actualizar datos del cliente");
        principalControlador.cerrarVentana(btnActualizarDatos);
    }

    @FXML
    public void irBilleteraVirtual(ActionEvent event) {
        principalControlador.navegarVentana("/billeteraVirtual.fxml", "Billetera Virtual");
        principalControlador.cerrarVentana(btnBilleteraVirtual);
    }

    @FXML
    public void irPanelUsuario(ActionEvent event) {
        principalControlador.navegarVentana("/panelUsuario.fxml", "Panel del usuario");
        principalControlador.cerrarVentana(btnRegresar);
    }

    @Override
    public void notificar() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
