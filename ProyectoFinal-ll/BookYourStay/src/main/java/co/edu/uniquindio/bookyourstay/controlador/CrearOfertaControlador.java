package co.edu.uniquindio.bookyourstay.controlador;

import co.edu.uniquindio.bookyourstay.controlador.observador.Observable;
import co.edu.uniquindio.bookyourstay.modelo.Alojamiento;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class CrearOfertaControlador implements Observable, Initializable {

    @FXML private ComboBox<Alojamiento> alojamientoSeleccionado;
    @FXML private DatePicker fechaInicioPicker;
    @FXML private DatePicker fechaFinPicker;
    @FXML private ComboBox<Float> porcentajeDescuento;
    @FXML private Button btnCrearOferta;
    @FXML private Button btnEliminarOferta;
    @FXML private Button btnRegresar;
    @FXML private TableView<Alojamiento> tablaOfertas;
    @FXML private TableColumn<Alojamiento, String> colNombre;
    @FXML private TableColumn<Alojamiento, LocalDate> colFechaInicio;
    @FXML private TableColumn<Alojamiento, LocalDate> colFechaFin;
    @FXML private TableColumn<Alojamiento, Float> colDescuento;
    private final PrincipalControlador principalControlador;

    public CrearOfertaControlador(){
        principalControlador = PrincipalControlador.getInstancia();
    }

    private boolean validarCampos() {
        if(alojamientoSeleccionado.getItems().isEmpty() || fechaInicioPicker.getValue() == null ||
                fechaFinPicker.getValue() == null){
            principalControlador.mostrarAlerta("Ningún campo puede estar vacío", Alert.AlertType.ERROR);
            return false;
        }
        if (fechaInicioPicker.getValue().isBefore(LocalDate.now()) || fechaFinPicker.getValue().isBefore(LocalDate.now())) {
            principalControlador.mostrarAlerta("La fecha seleccionada debe ser igual o superior al día de hoy", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }

    @FXML
    public void crearOferta() {
        if (!validarCampos()) return;

        try {
            Alojamiento alojamiento = alojamientoSeleccionado.getValue();
            LocalDate fechaInicio = fechaInicioPicker.getValue();
            LocalDate fechaFin = fechaFinPicker.getValue();
            float descuento = porcentajeDescuento.getValue() / 100;
            principalControlador.crearOfertaEspecial(alojamiento, fechaInicio, fechaFin, descuento);

            principalControlador.mostrarAlerta("La oferta se creó exitosamente.", Alert.AlertType.INFORMATION);
            ObservableList<Alojamiento> ofertasEspeciales = principalControlador.listarOfertasEspeciales();
            tablaOfertas.setItems(ofertasEspeciales);
            tablaOfertas.refresh();
        } catch (Exception e) {
            principalControlador.mostrarAlerta("Error: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    @FXML
    public void editarOferta() {
        if (!validarCampos()) return;
        try {
            Alojamiento alojamiento = alojamientoSeleccionado.getValue();
            LocalDate nuevaFechaInicio = fechaInicioPicker.getValue();
            LocalDate nuevaFechaFin = fechaFinPicker.getValue();
            float nuevoDescuento = porcentajeDescuento.getValue() / 100;
            principalControlador.editarOferta(alojamiento, nuevaFechaInicio, nuevaFechaFin, nuevoDescuento);
            principalControlador.mostrarAlerta("La oferta se actualizó exitosamente.", Alert.AlertType.INFORMATION);
            tablaOfertas.setItems(principalControlador.listarOfertasEspeciales());

        } catch (Exception e) {
            principalControlador.mostrarAlerta("Error al editar la oferta: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    @FXML
    public void eliminarOferta() throws Exception {
        Alojamiento seleccion = tablaOfertas.getSelectionModel().getSelectedItem();
        if (seleccion == null) {
            principalControlador.mostrarAlerta("Seleccione una oferta para eliminar.", Alert.AlertType.ERROR);
        } else {
            principalControlador.eliminarOferta(seleccion.getNombre());
            principalControlador.mostrarAlerta("La oferta fue eliminada exitosamente.", Alert.AlertType.INFORMATION);
            ObservableList<Alojamiento> ofertasEspeciales = principalControlador.listarOfertasEspeciales();
            tablaOfertas.setItems(ofertasEspeciales);
            tablaOfertas.refresh();
        }
    }

    public void configurarColumnasTabla() {
        colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        colFechaInicio.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getFechaInicioOferta()));
        colFechaFin.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getFechaFinOferta()));
        colDescuento.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDescuento()));
    }

    @FXML
    public void irPanelAdmin() {
        principalControlador.navegarVentana("/panelAdministrador.fxml", "Panel del administrador");
        principalControlador.cerrarVentana(btnRegresar);
    }

    @Override
    public void notificar() {
    }

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarColumnasTabla();
        try {
            ObservableList<Alojamiento> alojamientosDisponibles = FXCollections.observableArrayList(principalControlador.listarAlojamientos(""));
            alojamientoSeleccionado.getItems().setAll(alojamientosDisponibles);
            alojamientoSeleccionado.setCellFactory(param -> new ListCell<Alojamiento>() {
                @Override
                protected void updateItem(Alojamiento item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getNombre());
                    }
                }
            });
            alojamientoSeleccionado.setButtonCell(new ListCell<Alojamiento>() {
                @Override
                protected void updateItem(Alojamiento item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getNombre());
                    }
                }
            });
            porcentajeDescuento.setItems(FXCollections.observableArrayList(10f, 20f, 30f, 50f));
            ObservableList<Alojamiento> ofertasEspeciales = principalControlador.listarOfertasEspeciales();
            tablaOfertas.setItems(ofertasEspeciales);

        } catch (Exception e) {
            principalControlador.mostrarAlerta("Error al cargar los alojamientos: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}
