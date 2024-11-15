package co.edu.uniquindio.bookyourstay.controlador;

import co.edu.uniquindio.bookyourstay.controlador.observador.Observable;
import co.edu.uniquindio.bookyourstay.modelo.Alojamiento;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoAlojamiento;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CrearOfertaControlador implements Observable, Initializable {

    @FXML private ComboBox<Alojamiento> alojamientoSeleccionado;
    @FXML private Button btnCrearOferta;
    @FXML private Button btnEliminarOferta;
    @FXML private TableColumn<Alojamiento, String> colFechaFin;
    @FXML private TableColumn<Alojamiento, String> colFechaInicio;
    @FXML private TableColumn<Alojamiento, String> colNombre;
    @FXML private TableColumn<Alojamiento, String> colValorConDescuento;
    @FXML private DatePicker fechaFinPicker;
    @FXML private DatePicker fechaInicioPicker;
    @FXML private ComboBox<String> porcentajeDescuento;
    private final PrincipalControlador principalControlador;

    public CrearOfertaControlador(){
        principalControlador = PrincipalControlador.getInstancia();
    }
    @FXML
    public void crearOferta() {
        if(alojamientoSeleccionado.getItems().isEmpty() || fechaInicioPicker.getValue() == null ||
                fechaFinPicker.getValue() == null || porcentajeDescuento.getItems().isEmpty()){
            principalControlador.mostrarAlerta("Ningún campo puede estar vacío.", Alert.AlertType.ERROR);
        }
    }



    @Override
    public void notificar() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
