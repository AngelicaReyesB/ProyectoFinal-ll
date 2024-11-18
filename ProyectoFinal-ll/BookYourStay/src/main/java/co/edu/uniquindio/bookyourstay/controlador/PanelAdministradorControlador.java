package co.edu.uniquindio.bookyourstay.controlador;

import co.edu.uniquindio.bookyourstay.controlador.observador.Observable;
import co.edu.uniquindio.bookyourstay.modelo.Alojamiento;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

//funciona correctamente
public class PanelAdministradorControlador implements Observable, Initializable {

    @FXML private TableView<Alojamiento> TableAlojamientos;
    @FXML private Button btnAlojamientosRentables;
    @FXML private Button btnAlojamientosPopulares;
    @FXML private Button btnCerrarSesion;
    @FXML private Button btnCrearAlojamientos;
    @FXML private Button btnCrearOfertas;
    @FXML private Button btnVerEstadisticas;
    @FXML private TableColumn<Alojamiento, String> colCapacidad;
    @FXML private TableColumn<Alojamiento, String> colCiudad;
    @FXML TableColumn<Alojamiento, ImageView> colImagen = new TableColumn<>("Imagen");
    @FXML private TableColumn<Alojamiento, String> colNombre;
    @FXML private TableColumn<Alojamiento, String> colServicios;
    @FXML private TableColumn<Alojamiento, String> colValorNoche;
    private final PrincipalControlador principalControlador;
    private Observable observable;

    public PanelAdministradorControlador(){
        principalControlador = PrincipalControlador.getInstancia();
        System.out.println(principalControlador.getBookYourStay().getAlojamientos());
    }

    @FXML
    void alojamientosPopulares() {
        principalControlador.navegarVentana("/alojamientosPopulares.fxml", "Alojamientos populares");
        principalControlador.cerrarVentana(btnAlojamientosPopulares);
    }

    @FXML
    void alojamientosRentables() {
        principalControlador.navegarVentana("/alojamientosRentables.fxml", "Alojamientos rentables");
        principalControlador.cerrarVentana(btnAlojamientosRentables);
    }

    @FXML
    public void cerrarSesion() {
        principalControlador.navegarVentana("/inicio.fxml", "Inicio.");
        principalControlador.cerrarVentana(btnCerrarSesion);
    }

    @FXML
    public void crearAlojamientos() {
        principalControlador.navegarVentana("/crearAlojamiento.fxml", "Crear Alojamiento");
        principalControlador.cerrarVentana(btnCrearAlojamientos);
    }

    @FXML
    public void crearOfertas() {
        principalControlador.navegarVentana("/crearOferta.fxml", "Crear ofertas");
        principalControlador.cerrarVentana(btnCrearOfertas);
    }

    @FXML
    void verEstadisticas() {
        principalControlador.navegarVentana("/verEstadisticas.fxml", "Ver estadísticas");
        principalControlador.cerrarVentana(btnVerEstadisticas);
    }

    public void actualizarTabla(ArrayList listaAlojamientos){
        colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        colCiudad.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTipoCiudad().toString()));
        colCapacidad.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getCapacidadMaxima())));
        colValorNoche.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getValorNoche())));
        colServicios.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getServiciosIncluidos().toString()));
        colImagen.setCellValueFactory(cellData -> {
            String imagenUrl = cellData.getValue().getImagen();  // Obtén la URL de la imagen
            if (imagenUrl != null && !imagenUrl.isEmpty()) {
                Image image = new Image(imagenUrl);  // Cargar la imagen
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(100);  // Establecer el tamaño de la imagen
                imageView.setFitHeight(100);
                return new SimpleObjectProperty<>(imageView);  // Devolver el ImageView
            } else {
                return new SimpleObjectProperty<>(new ImageView());  // Devuelve un ImageView vacío si no hay imagen
            }
        });

        TableAlojamientos.setItems(FXCollections.observableArrayList(listaAlojamientos));
    }
    public void inicializarValores(PrincipalControlador principalControlador){
        try {
            if(principalControlador != null){
                actualizarTabla((ArrayList) principalControlador.getSesion().getAlojamientos());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void inicializarObservable(Observable observable) {
        this.observable = observable;
    }

    @Override
    public void notificar() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inicializarValores(principalControlador);
    }
}

