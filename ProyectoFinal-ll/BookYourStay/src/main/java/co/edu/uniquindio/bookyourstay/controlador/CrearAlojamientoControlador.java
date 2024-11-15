package co.edu.uniquindio.bookyourstay.controlador;

import co.edu.uniquindio.bookyourstay.controlador.observador.Observable;
import co.edu.uniquindio.bookyourstay.modelo.Alojamiento;
import co.edu.uniquindio.bookyourstay.modelo.Sesion;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoAlojamiento;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoCiudad;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

//falta cargar la imagen
public class CrearAlojamientoControlador implements Observable, Initializable {

    @FXML private CheckBox activoCheck;
    @FXML private Button imagen;
    @FXML private Label imagenCargada;
    @FXML private ComboBox<TipoCiudad> cbCiudad;
    @FXML private ComboBox<String> cbServicios;
    @FXML private ComboBox<TipoAlojamiento> cbTipoAlojamiento;
    @FXML private TableColumn<Alojamiento, String> colCapacidad;
    @FXML private TableColumn<Alojamiento, String> colCiudad;
    @FXML private TableColumn<Alojamiento, String> colDescripcion;
    @FXML private TableColumn<Alojamiento, String> colEstado;
    @FXML private TableColumn<Alojamiento, String> colFecha;
    @FXML private TableColumn<Alojamiento, String> colImagen;
    @FXML private TableColumn<Alojamiento, String> colNombre;
    @FXML private TableColumn<Alojamiento, String> colServicios;
    @FXML private TableColumn<Alojamiento, String > colTipo;
    @FXML private TableColumn<Alojamiento, String> colValor;
    @FXML private TableView<Alojamiento> tablaAlojamientos;
    @FXML private DatePicker fechaInstanciaPicker;
    @FXML private TextField txtCapacidadMaxima;
    @FXML private TextArea txtDescripcion;
    @FXML private TextField txtNombre;
    @FXML private TextField txtValorNoche;
    private String imagenSeleccionada;
    private final PrincipalControlador principalControlador;
    private Observable observable;
    private final Sesion sesion = Sesion.getInstancia();

    public CrearAlojamientoControlador(){
        principalControlador = PrincipalControlador.getInstancia();
        System.out.println(principalControlador.getBookYourStay().getAlojamientos());
    }

    private void abrirFileChooser() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg"));
        File imagen = fileChooser.showOpenDialog(null);
        System.out.println(imagen.toString());

        imagenSeleccionada = imagen.toURI().toString();
        imagenCargada.setText("Imagen cargada exitosamente.");
    }

    @FXML
    public void cargarImagen() {
        abrirFileChooser();
    }

    public boolean cambioFecha() {
        System.out.println(fechaInstanciaPicker.getValue());
        LocalDate fechaActual = LocalDate.now();
        boolean isBefore = false;
        if(fechaInstanciaPicker.getValue().isBefore(fechaActual)){
            principalControlador.mostrarAlerta("La fecha seleccionada debe ser igual o superior al día de hoy", Alert.AlertType.WARNING);
            isBefore = true;
        }
        return isBefore;
    }

    @FXML
    public void registrarAlojamiento() {
        if(txtNombre.getText().isEmpty() || txtDescripcion.getText().isEmpty() ||
                txtCapacidadMaxima.getText().isEmpty() || txtValorNoche.getText().isEmpty() ||
                fechaInstanciaPicker.getValue() == null || cbCiudad.getItems().isEmpty() ||
                cbServicios.getItems().isEmpty() || cbTipoAlojamiento.getItems().isEmpty() ||
                imagenSeleccionada == null) {

            principalControlador.mostrarAlerta("Ningún campo puede estar vacío", Alert.AlertType.ERROR);
        } else if(cambioFecha()) {
            principalControlador.mostrarAlerta("La fecha seleccionada debe ser igual o superior al día de hoy", Alert.AlertType.WARNING);
        } else {
            try {
                String valorNocheText = txtValorNoche.getText();
                String capacidadMaximaText = txtCapacidadMaxima.getText();

                float valorNoche = Float.parseFloat(valorNocheText);
                int capacidadMaxima = Integer.parseInt(capacidadMaximaText);

                TipoAlojamiento tipoAlojamiento = cbTipoAlojamiento.getValue();
                TipoCiudad tipoCiudad = cbCiudad.getValue();
                boolean activo = activoCheck.isSelected();
                Alojamiento alojamientoCreado = principalControlador.crearAlojamiento(txtNombre.getText(), txtDescripcion.getText(), imagenSeleccionada, fechaInstanciaPicker.getValue(), valorNoche, capacidadMaxima, cbServicios.getItems(), tipoAlojamiento, tipoCiudad, activo);

                if(alojamientoCreado != null){
                    observable.notificar();
                    actualizarTabla((ArrayList<Alojamiento>) principalControlador.getSesion().getAlojamientos());
                    principalControlador.mostrarAlerta("Alojamiento creado correctamente", Alert.AlertType.INFORMATION);
                }
            } catch (NumberFormatException e) {
                principalControlador.mostrarAlerta("Los valores de 'Valor por noche' o 'Capacidad máxima' no son válidos. Por favor ingrese números.", Alert.AlertType.ERROR);
            } catch (Exception e) {
                principalControlador.mostrarAlerta("Ocurrió un error al registrar el alojamiento.", Alert.AlertType.ERROR);
            }
        }
    }

    public void inicializarValores(PrincipalControlador principalControlador) {
        try {
            if (principalControlador != null) {
                actualizarTabla((ArrayList<Alojamiento>) principalControlador.getSesion().getAlojamientos());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actualizarTabla(ArrayList<Alojamiento> alojamientos){
        colCapacidad.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getCapacidadMaxima())));
        colValor.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getValorNoche())));
        colCiudad.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTipoCiudad().toString()));
        colDescripcion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescripcion()));
        colEstado.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isActivo() ? "Activo" : "Inactivo"));
        colFecha.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFechaEstancia().toString()));
        colImagen.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getImagen()));
        colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        colServicios.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getServiciosIncluidos().toString()));
        colTipo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTipoAlojamiento().toString()));

        tablaAlojamientos.setItems(FXCollections.observableArrayList());
    }

    @FXML
     public void editarAlojamiento() {
        Alojamiento alojamientoSeleccionado = tablaAlojamientos.getSelectionModel().getSelectedItem();
        if(alojamientoSeleccionado == null){
            principalControlador.mostrarAlerta("Selecciona un alojamiento para actualizar.", Alert.AlertType.WARNING);
            return;
        }
        activoCheck.setSelected(alojamientoSeleccionado.isActivo());
        try {
            alojamientoSeleccionado.setActivo(activoCheck.isSelected());
            Alojamiento alojamientoModificado = principalControlador.actualizarAlojamiento(alojamientoSeleccionado);
            System.out.println(alojamientoModificado);
            observable.notificar();
            principalControlador.mostrarAlerta("Alojamiento actualizado exitosamente.", Alert.AlertType.CONFIRMATION);
        } catch (Exception e) {
            principalControlador.mostrarAlerta("El alojamiento no se pudo actualizar." + e.getMessage(), Alert.AlertType.INFORMATION);
        }

    }

    @FXML
    public void eliminarAlojamiento() throws Exception {
        Alojamiento alojamientoSeleccionado = tablaAlojamientos.getSelectionModel().getSelectedItem();
        if (alojamientoSeleccionado == null) {
            principalControlador.mostrarAlerta("Selecciona una alojamiento para eliminar.", Alert.AlertType.WARNING);
            return;
        }
        principalControlador.eliminarAlojamiento(alojamientoSeleccionado);
        actualizarTabla((ArrayList<Alojamiento>) principalControlador.getSesion().getAlojamientos());
        principalControlador.mostrarAlerta("Alojamiento eliminado correctamente.", Alert.AlertType.CONFIRMATION);
    }

    @Override
    public void notificar() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inicializarValores(principalControlador);
        ObservableList<String> serviciosIncluidos = FXCollections.observableArrayList("WiFi", "Desayuno", "Piscina", "Gimnasio", "Parking");
        cbServicios.setItems(serviciosIncluidos);
        cbTipoAlojamiento.setItems(FXCollections.observableArrayList(TipoAlojamiento.values()));
        cbCiudad.setItems(FXCollections.observableArrayList(TipoCiudad.values()));
    }
}
