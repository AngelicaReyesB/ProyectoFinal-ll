package co.edu.uniquindio.bookyourstay.controlador;

import co.edu.uniquindio.bookyourstay.controlador.observador.Observable;
import co.edu.uniquindio.bookyourstay.modelo.Alojamiento;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoAlojamiento;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoCiudad;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PanelUsuarioControlador implements Observable, Initializable {

    @FXML private TableColumn<Alojamiento, ImageView> colImagen;
    @FXML private TableColumn<Alojamiento, String> colNombre;
    @FXML private TableColumn<Alojamiento, String> colPrecio;
    @FXML private TableColumn<Alojamiento, String> colTipoAlojamiento;
    @FXML private TableView<Alojamiento> tablaAlojamientosDisponibles;
    @FXML private Button verDetalles;
    private final PrincipalControlador principalControlador;
    private ObservableList<Alojamiento> listaAlojamientos;
    private Observable observable;

    public PanelUsuarioControlador(){
        principalControlador = PrincipalControlador.getInstancia();
    }

    @FXML
    void irDetallesAlojamiento() {
        Alojamiento alojamientoSeleccionado = tablaAlojamientosDisponibles.getSelectionModel().getSelectedItem();
        if (alojamientoSeleccionado == null) {
            principalControlador.mostrarAlerta("Debes seleccionar un alojamiento antes de continuar.", Alert.AlertType.WARNING);
            return;
        }
        principalControlador.getSesion().setAlojamientoDetalle(alojamientoSeleccionado);
        principalControlador.navegarVentana("/detallesAlojamiento.fxml", "Detalles del alojamiento");
        principalControlador.cerrarVentana(verDetalles);
    }

    private void configurarTabla() {
        colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        colPrecio.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getValorNoche())));
        colTipoAlojamiento.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTipoAlojamiento().toString()));
        colImagen.setCellValueFactory(cellData -> {
            String imagenUrl = cellData.getValue().getImagen(); // Asume que getImagen devuelve la URL de la imagen
            if (imagenUrl != null && !imagenUrl.isEmpty()) {
                // Crear el ImageView si hay una URL válida
                Image image = new Image(imagenUrl, 100, 100, true, true); // Cargar la imagen con tamaño ajustado
                ImageView imageView = new ImageView(image);
                return new SimpleObjectProperty<>(imageView); // Devolver ImageView
            } else {
                return new SimpleObjectProperty<>(new ImageView());
            }
        });
    }

    private void actualizarTabla() {
        tablaAlojamientosDisponibles.setItems(FXCollections.observableArrayList(principalControlador.getSesion().getAlojamientos()));
    }

    @Override
    public void notificar() {
        String ciudadSeleccionada = principalControlador.getSesion().getCiudadSeleccionada();
        String tipoSeleccionado = principalControlador.getSesion().getTipoAlojamientoSeleccionado();

        List<Alojamiento> alojamientosFiltrados = new ArrayList<>();

        try {
            if (ciudadSeleccionada != null && !ciudadSeleccionada.isEmpty()) {
                alojamientosFiltrados = principalControlador.getBookYourStay()
                        .listarAlojamientos(TipoCiudad.valueOf(ciudadSeleccionada));
            } else {
                alojamientosFiltrados = principalControlador.getBookYourStay().listarAlojamientos();
            }

            if (tipoSeleccionado != null && !tipoSeleccionado.isEmpty()) {
                alojamientosFiltrados = alojamientosFiltrados.stream()
                        .filter(alojamiento -> alojamiento.getTipoAlojamiento() == TipoAlojamiento.valueOf(tipoSeleccionado))
                        .toList();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        listaAlojamientos.setAll(alojamientosFiltrados);
        actualizarTabla();
    }

    public void inicializarObservable(Observable observable) {
        this.observable = observable;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listaAlojamientos = FXCollections.observableArrayList();
        tablaAlojamientosDisponibles.setItems(listaAlojamientos);
        principalControlador.registrarObservador(this);
        configurarTabla();
        actualizarTabla();
    }

}