package co.edu.uniquindio.bookyourstay.controlador;

import co.edu.uniquindio.bookyourstay.controlador.observador.Observable;
import co.edu.uniquindio.bookyourstay.modelo.Alojamiento;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoAlojamiento;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoCiudad;
import javafx.beans.property.SimpleObjectProperty;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

//modificar los servicios, editar
public class CrearAlojamientoControlador implements Observable, Initializable {

    @FXML private CheckBox activoCheck;
    @FXML private Label imagenCargada;
    @FXML private ComboBox<TipoCiudad> cbCiudad;
    @FXML private Button btnRegresar;
    @FXML private ComboBox<String> cbServicios;
    @FXML private ComboBox<TipoAlojamiento> cbTipoAlojamiento;
    @FXML private TableColumn<Alojamiento, String> colCapacidad;
    @FXML private TableColumn<Alojamiento, String> colCiudad;
    @FXML private TableColumn<Alojamiento, String> colDescripcion;
    @FXML private TableColumn<Alojamiento, String> colEstado;
    @FXML private TableColumn<Alojamiento, String> colFecha;
    @FXML TableColumn<Alojamiento, ImageView> colImagen = new TableColumn<>("Imagen");
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
    private Alojamiento alojamientoActual;
    private final PrincipalControlador principalControlador;


    public CrearAlojamientoControlador(){
        principalControlador = PrincipalControlador.getInstancia();
    }


    @FXML
    private void cargarImagen() {
        abrirFileChooser();
    }

    private void abrirFileChooser() {

        // Crear un FileChooser para seleccionar la imagen
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen");

        // Filtrar los archivos que se pueden seleccionar
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );

        // Mostrar el FileChooser y obtener la imagen seleccionada
        File imagen = fileChooser.showOpenDialog(null);
        System.out.println(imagen.toString());

        imagenSeleccionada = imagen.toURI().toString();
        imagenCargada.setText("Imagen cargada exitosamente.");
    }

    private boolean validarCampos() {
        if (txtNombre.getText().isEmpty() || txtDescripcion.getText().isEmpty() ||
                txtCapacidadMaxima.getText().isEmpty() || txtValorNoche.getText().isEmpty() ||
                fechaInstanciaPicker.getValue() == null || cbCiudad.getValue() == null ||
                cbServicios.getValue() == null || cbTipoAlojamiento.getValue() == null ||
                imagenSeleccionada == null) {
            principalControlador.mostrarAlerta("Ningún campo puede estar vacío", Alert.AlertType.ERROR);
            return false;
        }
        if (fechaInstanciaPicker.getValue().isBefore(LocalDate.now())) {
            principalControlador.mostrarAlerta("La fecha seleccionada debe ser igual o superior al día de hoy", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }

    @FXML
    public void registrarAlojamiento() {
        if (!validarCampos()) return;

        try {
            float valorNoche = Float.parseFloat(txtValorNoche.getText());
            int capacidadMaxima = Integer.parseInt(txtCapacidadMaxima.getText());

            TipoAlojamiento tipoAlojamiento = cbTipoAlojamiento.getValue();
            TipoCiudad tipoCiudad = cbCiudad.getValue();
            boolean activo = activoCheck.isSelected();

            Alojamiento alojamientoCreado = principalControlador.crearAlojamiento(
                    txtNombre.getText(),
                    txtDescripcion.getText(),
                    imagenSeleccionada,
                    fechaInstanciaPicker.getValue(),
                    valorNoche,
                    capacidadMaxima,
                    cbServicios.getItems(),
                    tipoAlojamiento,
                    tipoCiudad,
                    activo
            );
            limpiarCampos();

            if (alojamientoCreado != null) {
                principalControlador.notificarObservadores();
                actualizarTabla((ArrayList<Alojamiento>) principalControlador.getSesion().getAlojamientos());
                principalControlador.mostrarAlerta("Alojamiento creado correctamente", Alert.AlertType.INFORMATION);
            }
        } catch (NumberFormatException e) {
            principalControlador.mostrarAlerta("Los valores de 'Valor por noche' o 'Capacidad máxima' no son válidos. Por favor ingrese números.", Alert.AlertType.ERROR);
        } catch (Exception e) {
            principalControlador.mostrarAlerta("Ocurrió un error al registrar el alojamiento.", Alert.AlertType.ERROR);
        }
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtDescripcion.clear();
        txtValorNoche.clear();
        txtCapacidadMaxima.clear();
        cbTipoAlojamiento.getSelectionModel().clearSelection();
        cbCiudad.getSelectionModel().clearSelection();
        activoCheck.setSelected(false);
        cbServicios.getSelectionModel().clearSelection();
        imagenCargada = null;
        imagenSeleccionada = null;
        fechaInstanciaPicker.setValue(null);
    }

    public void actualizarTabla(ArrayList<Alojamiento> alojamientos) {
        colCapacidad.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getCapacidadMaxima())));
        colValor.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getValorNoche())));
        colCiudad.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTipoCiudad().toString()));
        colDescripcion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescripcion()));
        colEstado.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().isActivo() ? "Activo" : "Inactivo"));
        colFecha.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFechaEstancia().toString()));
        colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        colServicios.setCellValueFactory(cellData -> new SimpleStringProperty(String.join(", ", cellData.getValue().getServiciosIncluidos())));
        colTipo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTipoAlojamiento().toString()));

        // Aquí se crea el ImageView a partir de la URL de la imagen
        colImagen.setCellValueFactory(cellData -> {
            String imagenUrl = cellData.getValue().getImagen(); // Esto asume que la URL está almacenada en getImagen
            if (imagenUrl != null) {
                Image image = new Image(imagenUrl);  // Cargar la imagen
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(100); // Ajustar el tamaño de la imagen
                imageView.setFitHeight(100);
                return new SimpleObjectProperty<>(imageView); // Devolver un ImageView
            } else {
                return new SimpleObjectProperty<>(new ImageView()); // Devolver un ImageView vacío si no hay imagen
            }
        });




        if (alojamientos != null && !alojamientos.isEmpty()) {
            tablaAlojamientos.setItems(FXCollections.observableArrayList(alojamientos));
        } else {
            tablaAlojamientos.setItems(FXCollections.observableArrayList());
        }
    }


    @FXML
    public void editarAlojamiento() {
        alojamientoActual = tablaAlojamientos.getSelectionModel().getSelectedItem();
        if (alojamientoActual == null) {
            principalControlador.mostrarAlerta("Selecciona un alojamiento para actualizar.", Alert.AlertType.WARNING);
            return;
        }
        activoCheck.setSelected(alojamientoActual.isActivo());
        try {
            alojamientoActual.setActivo(activoCheck.isSelected());
            Alojamiento alojamientoModificado = principalControlador.actualizarAlojamiento(alojamientoActual);
            principalControlador.notificarObservadores();
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

    private void actualizarServiciosDisponibles() {
        ObservableList<String> serviciosActualizados = FXCollections.observableArrayList();

        if (cbTipoAlojamiento.getValue() != null) {
            // Ejemplo de lógica para cambiar servicios dependiendo del tipo de alojamiento
            if (cbTipoAlojamiento.getValue() == TipoAlojamiento.CASA) {
                serviciosActualizados.addAll("WiFi", "Piscina", "Gimnasio");
            } else if (cbTipoAlojamiento.getValue() == TipoAlojamiento.APARTAMENTO) {
                serviciosActualizados.addAll("WiFi", "Parking");
            }
        }
        cbServicios.setItems(serviciosActualizados);
    }

    @FXML
    public void irPanelAdministrador() {
        principalControlador.navegarVentana("/panelAdministrador.fxml", "Panel del administrador");
        principalControlador.cerrarVentana(btnRegresar);
    }

    @Override
    public void notificar() {
        actualizarTabla((ArrayList<Alojamiento>) principalControlador.getSesion().getAlojamientos());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> serviciosIncluidos = FXCollections.observableArrayList("WiFi", "Desayuno", "Piscina", "Gimnasio", "Parking");
        cbServicios.setItems(serviciosIncluidos);
        cbTipoAlojamiento.setOnAction(event -> actualizarServiciosDisponibles());
        cbCiudad.setItems(FXCollections.observableArrayList(TipoCiudad.values()));
        cbTipoAlojamiento.setItems(FXCollections.observableArrayList(TipoAlojamiento.values()));
        PrincipalControlador.getInstancia().registrarObservador(this);
    }
}
