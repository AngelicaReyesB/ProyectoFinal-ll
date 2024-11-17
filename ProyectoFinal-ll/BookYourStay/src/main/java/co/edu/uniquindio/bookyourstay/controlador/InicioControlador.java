package co.edu.uniquindio.bookyourstay.controlador;

import co.edu.uniquindio.bookyourstay.controlador.observador.Observable;
import co.edu.uniquindio.bookyourstay.modelo.Alojamiento;
import co.edu.uniquindio.bookyourstay.modelo.Cliente;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

//hecho, solo falta des comentar cuando se añada en el panel
public class InicioControlador implements Observable, Initializable {

    @FXML private HBox HBoxAlojamientosAleatorios;
    @FXML private Button btnIngresar;
    @FXML private TableColumn<Alojamiento, String> colCiudad;
    @FXML private TableColumn<Alojamiento, ImageView> colImagen;
    @FXML private TableColumn<Alojamiento, ImageView> colImagenOfertas;
    @FXML private TableColumn<Alojamiento, String> colNombre;
    @FXML private TableColumn<Alojamiento, String> colPrecio;
    @FXML private TextField correo;
    @FXML private Hyperlink olvidoPasswordLink;
    @FXML private TextField password;
    @FXML private TableView<Alojamiento> tablaAlojamientosAleatoria;
    @FXML private TableView<Alojamiento> tablaOfertasAlojamientos;
    private final PrincipalControlador principalControlador;

    public InicioControlador() {
        principalControlador = PrincipalControlador.getInstancia();
        System.out.println("Alojamientos inicio: " + principalControlador.getBookYourStay().getAlojamientos());
        System.out.println("Alojamientos sesión" + principalControlador.getBookYourStay().getAlojamientos());
        System.out.println("Clientes: " + principalControlador.getBookYourStay().getClientes());
        System.out.println("Facturas: " + principalControlador.getBookYourStay().getFacturas());
        System.out.println();
        System.out.println();
    }

    public void ingresar() {
        if (correo.getText().isEmpty() || password.getText().isEmpty()) {
            principalControlador.mostrarAlerta("Los campos de correo y contraseña son obligatorios.", Alert.AlertType.WARNING);
            correo.clear();
            password.clear();
        } else {
            try {
                boolean administrador = principalControlador.validarIngresoAdministrador(correo.getText(), password.getText());
                if (administrador) {
                    principalControlador.getSesion().setAdministrador(true);
                    principalControlador.navegarVentana("/panelAdministrador.fxml", "Panel del administrador.");
                    principalControlador.cerrarVentana(correo);
                } else {
                    Cliente cliente = principalControlador.validarUsuario(correo.getText(), password.getText());
                    if (cliente != null) {
                        validarCliente(cliente);
                    }
                }
                correo.clear();
                password.clear();
            } catch (Exception e) {
                principalControlador.mostrarAlerta(e.getMessage(), Alert.AlertType.ERROR);
                correo.clear();
                password.clear();
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

    @FXML
    public void recuperacionPassword() {
        principalControlador.navegarVentana("/recuperacionPassword.fxml", "Recuperar contraseña");
        principalControlador.cerrarVentana(olvidoPasswordLink);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @Override
    public void notificar() {

    }

}
