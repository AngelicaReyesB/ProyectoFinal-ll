package co.edu.uniquindio.bookyourstay.controlador;

import co.edu.uniquindio.bookyourstay.controlador.observador.Observable;
import co.edu.uniquindio.bookyourstay.modelo.Alojamiento;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor

public class CrearOfertaControlador implements Observable, Initializable {

    @FXML
    private ComboBox<Alojamiento> alojamientoSeleccionado;

    @FXML
    private DatePicker fechaInicioPicker;

    @FXML
    private DatePicker fechaFinPicker;

    @FXML
    private ComboBox<Float> porcentajeDescuento;

    @FXML
    private Button btnCrearOferta;

    @FXML
    private Button btnEliminarOferta;

    @FXML
    private TableView<Alojamiento> tablaOfertas;

    @FXML
    private TableColumn<Alojamiento, String> colNombre;

    @FXML
    private TableColumn<Alojamiento, LocalDate> colFechaInicioOferta;

    @FXML
    private TableColumn<Alojamiento, LocalDate> colFechaFinOferta;

    @FXML
    private TableColumn<Alojamiento, Float> colDescuento;

    private final ObservableList<Alojamiento> listaAlojamientos = FXCollections.observableArrayList();
    private final ObservableList<Alojamiento> listaOfertas = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configurarColumnasTabla();
        cargarAlojamientos();

        // Configurar porcentaje de descuento
        porcentajeDescuento.setItems(FXCollections.observableArrayList(10f, 20f, 30f, 50f));

        // Enlazar ofertas a la tabla
        tablaOfertas.setItems(listaOfertas);
    }

    @FXML
    public void crearOferta() {
        try {
            Alojamiento alojamiento = alojamientoSeleccionado.getValue();
            LocalDate fechaInicio = fechaInicioPicker.getValue();
            LocalDate fechaFin = fechaFinPicker.getValue();
            Float descuento = porcentajeDescuento.getValue();

            if (alojamiento == null || fechaInicio == null || fechaFin == null || descuento == null) {
                throw new Exception("Todos los campos son obligatorios.");
            }

            if (fechaInicio.isAfter(fechaFin)) {
                throw new Exception("La fecha de inicio debe ser anterior a la fecha de fin.");
            }

            // Configurar la oferta especial en el alojamiento
            alojamiento.setFechaInicioOferta(fechaInicio);
            alojamiento.setFechaFinOferta(fechaFin);
            alojamiento.setDescuento(descuento);

            // Añadir a la lista de ofertas
            if (!listaOfertas.contains(alojamiento)) {
                listaOfertas.add(alojamiento);
                mostrarAlerta("Oferta creada", "La oferta se creó exitosamente.", Alert.AlertType.INFORMATION);
            } else {
                throw new Exception("El alojamiento ya tiene una oferta creada.");
            }
        } catch (Exception e) {
            mostrarAlerta("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void eliminarOferta() {
        Alojamiento seleccion = tablaOfertas.getSelectionModel().getSelectedItem();
        if (seleccion != null) {
            seleccion.setFechaInicioOferta(null);
            seleccion.setFechaFinOferta(null);
            seleccion.setDescuento(0);
            listaOfertas.remove(seleccion);
            mostrarAlerta("Oferta eliminada", "La oferta fue eliminada exitosamente.", Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Error", "Seleccione una oferta para eliminar.", Alert.AlertType.ERROR);
        }
    }

    private void configurarColumnasTabla() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colFechaInicioOferta.setCellValueFactory(new PropertyValueFactory<>("fechaInicioOferta"));
        colFechaFinOferta.setCellValueFactory(new PropertyValueFactory<>("fechaFinOferta"));
        colDescuento.setCellValueFactory(new PropertyValueFactory<>("descuento"));
    }

    private void cargarAlojamientos() {
        // Datos ficticios para prueba
        listaAlojamientos.add(Alojamiento.builder()
                .nombre("Hotel Paradise")
                .valorNoche(200.0f)
                .tipoAlojamiento(co.edu.uniquindio.bookyourstay.modelo.enums.TipoAlojamiento.HOTEL)
                .build());

        listaAlojamientos.add(Alojamiento.builder()
                .nombre("Casa Relax")
                .valorNoche(150.0f)
                .tipoAlojamiento(co.edu.uniquindio.bookyourstay.modelo.enums.TipoAlojamiento.CASA)
                .build());

        alojamientoSeleccionado.setItems(listaAlojamientos);
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
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