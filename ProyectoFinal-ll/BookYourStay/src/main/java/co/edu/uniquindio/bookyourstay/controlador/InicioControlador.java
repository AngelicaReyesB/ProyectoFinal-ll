package co.edu.uniquindio.bookyourstay.controlador;

import co.edu.uniquindio.bookyourstay.controlador.observador.Observable;
import co.edu.uniquindio.bookyourstay.modelo.Alojamiento;
import co.edu.uniquindio.bookyourstay.modelo.Cliente;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

//hecho, solo falta des comentar cuando se añada en el panel
public class InicioControlador implements Observable, Initializable {

    @FXML private GridPane AlojamientosDisponiblesGridPane;
    @FXML private GridPane InicioSesion;
    @FXML private Button btnIngresar;
    @FXML private TextField correo;
    @FXML private HBox ofertasAlojamientosHbox;
    @FXML private Hyperlink olvidoPasswordLink;
    @FXML private TextField password;
    private final PrincipalControlador principalControlador;
    private Observable observable;

    public InicioControlador() {
        principalControlador = PrincipalControlador.getInstancia();
        System.out.println("Alojamientos inicio: " + principalControlador.getBookYourStay().getAlojamientos());
        System.out.println("Alojamientos sesión" + principalControlador.getBookYourStay().getAlojamientos());
        System.out.println("Clientes: " + principalControlador.getBookYourStay().getClientes());
        System.out.println("Facturas: " + principalControlador.getBookYourStay().getFacturas());
        System.out.println();
        System.out.println();
    }

    public void ingresar(){
        if(correo.getText().isEmpty() || password.getText().isEmpty()) {
            principalControlador.mostrarAlerta("Los campos de correo y contraseña son obligatorios.", Alert.AlertType.WARNING);
            correo.clear();
            password.clear();
        }else {
            try {
                boolean administrador = principalControlador.validarIngresoAdministrador(correo.getText(), password.getText());
                if(administrador){
                    principalControlador.getSesion().setAdministrador(true);
                    principalControlador.navegarVentana("/panelAdministrador.fxml", "Panel del administrador.");
                    principalControlador.cerrarVentana(correo);
                }else {
                    Cliente cliente = principalControlador.obtenerCliente(correo.getText());
                    if(cliente != null){
                        validarCliente(cliente);
                    }else {
                        ButtonType respuesta = principalControlador.mostrarAlertaConfirmacion("El usuario no existe. \n\n ¿Desea registrarse?", Alert.AlertType.INFORMATION);
                        if(respuesta == ButtonType.OK) {
                            principalControlador.navegarVentana("/registroCliente.fxml", "Registro usuario");
                            principalControlador.cerrarVentana(correo);
                        }else {
                            correo.clear();
                            password.clear();
                        }
                    }
                }
                correo.clear();
                password.clear();
            }catch (Exception e){
                principalControlador.mostrarAlerta(e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private void validarCliente(Cliente cliente){
        if(cliente.getCedula().equals(correo.getText()) && cliente.getPassword().equals(password.getText())){
            if(cliente.isEstadoCuenta()){
                principalControlador.getSesion().setCliente(cliente);
                principalControlador.navegarVentana("/panelCliente.fxml", "Panel del Cliente.");
                principalControlador.cerrarVentana(correo);
            }else {
                principalControlador.getSesion().setCliente(cliente);
                principalControlador.navegarVentana("/activarCuenta.fxml", "Activar Cuenta");
            }
        }else {
            principalControlador.mostrarAlerta("Los datos de ingreso son incorrectos", Alert.AlertType.WARNING);
            correo.clear();
            password.clear();
        }
    }

    private void alojamientoRandom(){
        try{
            if(!principalControlador.getBookYourStay().getAlojamientos().isEmpty()){
                int indice = (int)Math.floor (Math.random() * principalControlador.getBookYourStay().getAlojamientos().size());
                Alojamiento alojamientoRandom = principalControlador.getBookYourStay().getAlojamientos().get(indice);
                principalControlador.getSesion().setAlojamientoAleatorio(alojamientoRandom);
                System.out.println("EVENTO RAMDON: " + alojamientoRandom);
                //nombreAlojamiento.setText(alojamientoRandom.getNombre());
                //descripcionAlojamiento.setText(alojamientoRandom.getDescripcion());
                //valorNocheAlojamiento.setText(alojamientoRandom.getValorNoche());
                //capacidadMaximaAlojamiento.setText(alojamientoRandom.getCapacidadMaxima());
                //ciudadAojamiento.setText(alojamientoRandom.getTipoCiudad().toString());
                //imagenEvento.setImage(new javafx.scene.image.Image((alojamientoRandom.getImagen())));
            }
        }catch (Exception e){
            System.out.println("Error al buscar alojamiento aleatorio");
        }
    }

    private void mostrarAlojamientos(){
        try{
            llamarAlojamientos();
        }catch (Exception ignored){
        }
    }

    private void llamarAlojamientos(){
        try{
            ArrayList<Alojamiento> listaAlojamientos = new ArrayList<>();
            if (principalControlador.getSesion().getAlojamientos() != null){
                 listaAlojamientos = (ArrayList<Alojamiento>) principalControlador.getSesion().getAlojamientos();
            }else {
                principalControlador.getSesion().setAlojamientos(principalControlador.getBookYourStay().getAlojamientos());
                listaAlojamientos = (ArrayList<Alojamiento>) principalControlador.getSesion().getAlojamientos();
            }

        }catch (Exception e){
            System.out.println("Error al llamar los eventos" + e.getMessage());
        }
    }

    @FXML
    public void recuperacionPassword() {
        principalControlador.navegarVentana("/recuperacionPassword.fxml", "Recuperar contraseña");
        principalControlador.cerrarVentana(olvidoPasswordLink);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        alojamientoRandom();
        mostrarAlojamientos();
    }

    @Override
    public void notificar() {

    }

    public void inicializarObservable(Observable observable){
        this.observable = observable;
    }
}
