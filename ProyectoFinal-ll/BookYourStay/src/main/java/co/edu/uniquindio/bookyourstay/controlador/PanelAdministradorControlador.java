package co.edu.uniquindio.bookyourstay.controlador;

import co.edu.uniquindio.bookyourstay.controlador.observador.Observable;
import co.edu.uniquindio.bookyourstay.modelo.Alojamiento;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PanelAdministradorControlador implements Observable, Initializable {

    @FXML private TableView<Alojamiento> TableAlojamientos;
    @FXML private Button btnAlojaientosRentables;
    @FXML private Button btnAlojamientosPopulares;
    @FXML private Button btnCerrarSesion;
    @FXML private Button btnCrearAlojamientos;
    @FXML private Button btnCrearOfertas;
    @FXML private Button btnVerEstadisticas;
    @FXML private TableColumn<Alojamiento, String> colCapacidad;
    @FXML private TableColumn<Alojamiento, String> colCiudad;
    @FXML private TableColumn<Alojamiento, String> colImagen;
    @FXML private TableColumn<Alojamiento, String> colNombre;
    @FXML private TableColumn<Alojamiento, String> colServicios;
    @FXML private TableColumn<Alojamiento, String> colValorNoche;
    private final PrincipalControlador principalControlador;
    private Observable observable;

    public PanelAdministradorControlador(){
        principalControlador = PrincipalControlador.getInstancia();
        System.out.println(principalControlador.getBookYourStay().getAlojamientos());
    }

    public void inicializarObservable(Observable observable){
        this.observable = observable;
    }

    @FXML
    public void cerrarSesion(ActionEvent event) {
        principalControlador.navegarVentana("/inicio.fxml", "Inicio.");
        principalControlador.cerrarVentana(btnCerrarSesion);
    }

    @FXML
    public void crearAlojamientos(ActionEvent event) {
        principalControlador.navegarVentana("/crearAlojamiento.fxml", "Crear Alojamiento");
        principalControlador.cerrarVentana(btnCrearAlojamientos);
    }

    @FXML
    public void crearOfertas(ActionEvent event) {
        principalControlador.navegarVentana("/crearOfertas.fxml", "Crear ofertas");
        principalControlador.cerrarVentana(btnCrearOfertas);
    }

    public void actualizarTabla(ArrayList listaAlojamientos){
        //colCodigo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCodigo()));
        colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        colCiudad.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTipoCiudad().toString()));
        colCapacidad.setCellValueFactory(cellData -> new SimpleStringProperty());
        colValorNoche.setCellValueFactory(cellData -> new SimpleStringProperty());
        colServicios.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getServiciosIncluidos().toString()));

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

    @Override
    public void notificar() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inicializarValores(principalControlador);
    }
}

