package co.edu.uniquindio.bookyourstay.controlador;

import co.edu.uniquindio.bookyourstay.controlador.observador.Observable;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


public class PanelUsuarioControlador implements Observable, Initializable {

    @FXML private GridPane AlojamientosDisponiblesGridPane;
    @FXML private HBox ofertasAlojamientosHbox;
    @FXML private VBox mainVBox;

    private void cargarAlojamientosDisponibles() {

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                AlojamientosDisponiblesGridPane.add(new Label("Alojamiento " + (i * 3 + j + 1)), j, i);
            }
        }
    }

    private void cargarOfertasAlojamientos() {
        String[] ofertas = {
                "Oferta 1: Descuento en la primera noche",
                "Oferta 2: Estancia de 2x1",
                "Oferta 3: Desayuno gratis incluido"
        };

        for (String oferta : ofertas) {
            VBox ofertaBox = new VBox();
            ofertaBox.setSpacing(10);
            ofertaBox.setStyle("-fx-padding: 10; -fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-border-width: 1;");

            Label ofertaLabel = new Label(oferta);
            ofertaBox.getChildren().add(ofertaLabel);

            ofertasAlojamientosHbox.getChildren().add(ofertaBox);
        }
    }


    @Override
    public void notificar() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarAlojamientosDisponibles();
        cargarOfertasAlojamientos();
    }
}
