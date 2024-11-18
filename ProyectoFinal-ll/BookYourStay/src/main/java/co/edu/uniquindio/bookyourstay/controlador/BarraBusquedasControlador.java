package co.edu.uniquindio.bookyourstay.controlador;

import co.edu.uniquindio.bookyourstay.controlador.observador.Observable;
import co.edu.uniquindio.bookyourstay.modelo.Alojamiento;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoAlojamiento;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoCiudad;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class BarraBusquedasControlador implements Observable, Initializable {

    @FXML private ComboBox<TipoCiudad> ciudadAlojamiento;
    @FXML private ComboBox<String> rangoPrecios;
    @FXML private ComboBox<TipoAlojamiento> tipoAlojamiento;
    @FXML private TextField nombreAlojamiento;
    private final PrincipalControlador principalControlador;


    public BarraBusquedasControlador(){
        principalControlador = PrincipalControlador.getInstancia();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tipoAlojamiento.setItems(FXCollections.observableArrayList(TipoAlojamiento.values()));
        ciudadAlojamiento.setItems(FXCollections.observableArrayList(TipoCiudad.values()));
    }

    @FXML
    public void obtenerNombres() {
        ArrayList<Alojamiento> alojamientos;
        try {
            if(!nombreAlojamiento.getText().isEmpty() || !nombreAlojamiento.getText().isBlank()){
                alojamientos = principalControlador.listarAlojamientos(nombreAlojamiento.getText());
                principalControlador.getSesion().setAlojamientos(alojamientos);
                navegarInicio();
                System.out.println(principalControlador.getSesion().getAlojamientos());
            }
        } catch (Exception e){
            principalControlador.mostrarAlerta("No se pudo encontrar el alojamiento. " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void mostrarTodosAlojamientos() {
        try {
            ArrayList<Alojamiento> alojamientos = principalControlador.listarAlojamientos();
            principalControlador.getSesion().setAlojamientos(alojamientos);
            navegarInicio();
        } catch (Exception e){
            principalControlador.mostrarAlerta("No se puede realizar la busqueda. " + e.getMessage(), Alert.AlertType.ERROR)    ;
        }
    }

    @FXML
    public void obtenerAlojamientosCiudad() {
        System.out.println(ciudadAlojamiento.getValue());
        ArrayList<Alojamiento> alojamientos;
        TipoCiudad ciudad = ciudadAlojamiento.getValue();
        try {
            alojamientos = principalControlador.listarAlojamientos(ciudad);
            principalControlador.getSesion().setAlojamientos(alojamientos);
            navegarInicio();
            System.out.println("EN ACCION BARRA NAVEGACION: ");
            System.out.println(principalControlador.getSesion().getAlojamientos());
        }catch (Exception e){
            principalControlador.mostrarAlerta("No se pudo realizar la busqueda. " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void obtenerTipoAlojamiento() {
        System.out.println(tipoAlojamiento.getValue());
        ArrayList<Alojamiento> alojamientos;
        TipoAlojamiento tipoAlojamiento1 = tipoAlojamiento.getValue();
        try {
            alojamientos = principalControlador.listarAlojamientos(tipoAlojamiento1);
            principalControlador.getSesion().setAlojamientos(alojamientos);
            navegarInicio();
        }catch (Exception e){
            principalControlador.mostrarAlerta("No se pudo realizar la busqueda. " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void navegarInicio() {
        if (principalControlador.getSesion().isAdministrador()) {
            FXMLLoader loader = principalControlador.navegarVentana("/panelAdministrador.fxml", "Panel administrador");
            PanelAdministradorControlador controlador = loader.getController();
            controlador.inicializarObservable(this);
            principalControlador.cerrarVentana(ciudadAlojamiento);
        }
        if(!principalControlador.getSesion().isAdministrador() && principalControlador.getSesion().getCliente() == null) {
            FXMLLoader loader = principalControlador.navegarVentana("/inicio.fxml", "Inicio");
            InicioControlador controlador = loader.getController();
            controlador.inicializarObservable(this);
            principalControlador.cerrarVentana(ciudadAlojamiento);
        }

        if(principalControlador.getSesion().getCliente() != null) {
            FXMLLoader loader = principalControlador.navegarVentana("/panelUsuario.fxml", "Panel");
            PanelUsuarioControlador controlador = loader.getController();
            controlador.inicializarObservable(this);
            principalControlador.cerrarVentana(ciudadAlojamiento);
        }
    }


    @Override
    public void notificar() {

    }
}


