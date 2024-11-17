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
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class PanelUsuarioControlador implements Observable, Initializable {

    @FXML private TableColumn<Alojamiento, ImageView> colImagen;
    @FXML private TableColumn<Alojamiento, String> colNombre;
    @FXML private TableColumn<Alojamiento, String> colPrecio;
    @FXML private TableColumn<Alojamiento, String> colTipoAlojamiento;
    @FXML private HBox ofertasAlojamientosHbox;
    @FXML private TableView<Alojamiento> tablaAlojamientosDisponibles;
    @FXML private Button verDetalles;
    private final PrincipalControlador principalControlador;

    public PanelUsuarioControlador(){
        principalControlador = PrincipalControlador.getInstancia();
    }

    @FXML
    void irDetallesAlojamiento(ActionEvent event) {
        principalControlador.navegarVentana("/detallesAlojamiento.fxml", "Detalles de alojamiento");
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
        actualizarTabla();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        principalControlador.notificarObservadores();
        configurarTabla();
        actualizarTabla();
    }
}






//    @FXML private GridPane AlojamientosDisponiblesGridPane;
//    @FXML private HBox ofertasAlojamientosHbox;
//    @FXML private VBox mainVBox;
//
//    private void cargarAlojamientosDisponibles() {
//
//        for (int i = 0; i < 3; i++) {
//            for (int j = 0; j < 3; j++) {
//                AlojamientosDisponiblesGridPane.add(new Label("Alojamiento " + (i * 3 + j + 1)), j, i);
//            }
//        }
//    }
//
//    private void cargarOfertasAlojamientos() {
//        String[] ofertas = {
//                "Oferta 1: Descuento en la primera noche",
//                "Oferta 2: Estancia de 2x1",
//                "Oferta 3: Desayuno gratis incluido"
//        };
//
//        for (String oferta : ofertas) {
//            VBox ofertaBox = new VBox();
//            ofertaBox.setSpacing(10);
//            ofertaBox.setStyle("-fx-padding: 10; -fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-border-width: 1;");
//
//            Label ofertaLabel = new Label(oferta);
//            ofertaBox.getChildren().add(ofertaLabel);
//
//            ofertasAlojamientosHbox.getChildren().add(ofertaBox);
//        }
//    }