package co.edu.uniquindio.bookyourstay.controlador;

import co.edu.uniquindio.bookyourstay.controlador.observador.Observable;
import co.edu.uniquindio.bookyourstay.modelo.Alojamiento;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoAlojamiento;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoCiudad;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

//hecho
public class BarraBusquedasControlador implements Observable, Initializable {

    @FXML private Button btnBuscarAlojamiento;
    @FXML private Button btnTodosAlojamientos;
    @FXML private ComboBox<TipoCiudad> ciudadAlojamiento;
    @FXML private ComboBox<String> rangoPrecios;
    @FXML private ComboBox<TipoAlojamiento> tipoAlojamiento;
    private final PrincipalControlador principalControlador;
    private Observable observable;

    public BarraBusquedasControlador(){
        principalControlador = PrincipalControlador.getInstancia();
    }

    @FXML
    public void obtenerAlojamientos(){
        try {
            ArrayList<Alojamiento> alojamientos = principalControlador.listarAlojamientos();
            principalControlador.getSesion().setAlojamientos(alojamientos);
            navegarInicio();
        } catch (Exception e) {
            principalControlador.mostrarAlerta("Error al filtrar la búsqueda." + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void buscar() {
        obtenerAlojamientos();
    }

    @FXML
    public void obtenerTipoAlojamiento(){
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

    @FXML
    public void obtenerAlojamientosCiudad() {
        System.out.println(ciudadAlojamiento.getValue());
        ArrayList<Alojamiento> alojamientos;
        TipoCiudad ciudad = ciudadAlojamiento.getValue();
        try {
            alojamientos = principalControlador.listarAlojamientos(ciudad);
            principalControlador.getSesion().setAlojamientos(alojamientos);
            navegarInicio();
            System.out.println("ALOJAMIENTOS EN ACCIÓN BARRA NAVEGACIÓN: ");
            System.out.println(principalControlador.getSesion().getAlojamientos());
        }catch (Exception e){
            principalControlador.mostrarAlerta("No se pudo realizar la búsqueda. " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void navegarInicio(){
        if(principalControlador.getSesion().isAdministrador()){
            principalControlador.navegarVentana("/panelAdministrador.fxml", "Panel del administrador.");
            principalControlador.cerrarVentana(btnTodosAlojamientos);
        }
        if(!principalControlador.getSesion().isAdministrador() && principalControlador.getSesion().getCliente() == null){
            principalControlador.navegarVentana("/inicio.fxml", "Inicio");
            principalControlador.cerrarVentana(btnTodosAlojamientos);
        }
        if(principalControlador.getSesion().isAdministrador() && principalControlador.getSesion().getCliente() == null){
            principalControlador.navegarVentana("/panelUsuario.fxml", "Panel del usuario");
            principalControlador.cerrarVentana(btnTodosAlojamientos);
        }
    }

    @Override
    public void notificar() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tipoAlojamiento.setItems(FXCollections.observableArrayList(TipoAlojamiento.values()));
        ciudadAlojamiento.setItems(FXCollections.observableArrayList(TipoCiudad.values()));
    }
}
