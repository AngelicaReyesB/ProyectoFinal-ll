package co.edu.uniquindio.bookyourstay.controlador;

import co.edu.uniquindio.bookyourstay.controlador.observador.Observable;
import co.edu.uniquindio.bookyourstay.modelo.Alojamiento;
import co.edu.uniquindio.bookyourstay.modelo.Cliente;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class InicioControlador implements Observable, Initializable {


    @FXML private Label nombre;
    @FXML private Label capacidad;
    @FXML private Label ciudad;
    @FXML private TableColumn<Alojamiento, String> colCiudad;
    @FXML private TableColumn<Alojamiento, String> colNombre;
    @FXML private TableColumn<Alojamiento, String> colTipoAlojamiento;
    @FXML private TextField correo;
    @FXML private ImageView imagen;
    @FXML private Hyperlink olvidoPasswordLink;
    @FXML private PasswordField password;
    @FXML private Label precio;
    @FXML private TableView<Alojamiento> tablaOfertas;
    private Alojamiento alojamientoRandom;
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
                    principalControlador.notificarObservadores();
                    principalControlador.navegarVentana("/panelAdministrador.fxml", "Panel del administrador.");
                    principalControlador.cerrarVentana(correo);
                } else {
                    Cliente cliente = principalControlador.obtenerUsuario(correo.getText());
                    if (cliente != null) {
                        validarCliente(cliente);
                    } else {
                        principalControlador.mostrarAlerta("El cliente con el correo proporcionado no existe.", Alert.AlertType.WARNING);
                        correo.clear();
                        password.clear();
                    }
                }
                correo.clear();
                password.clear();
            } catch (Exception e) {
                principalControlador.mostrarAlerta(e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private void validarCliente(Cliente cliente) {
        if (cliente.getEmail().equals(correo.getText()) && cliente.getPassword().equals(password.getText())) {
            if (cliente.isEstadoCuenta()) {
                principalControlador.getSesion().setCliente(cliente);
                principalControlador.navegarVentana("/panelUsuario.fxml", "Panel usuario");
                principalControlador.cerrarVentana(correo);
            } else {
                principalControlador.getSesion().setCliente(cliente);
                principalControlador.navegarVentana("activarCuenta.fxml", "Activar cuenta");
            }
        } else {
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

    private void alojamientoRandom() {
        try {
            if(!principalControlador.getBookYourStay().getAlojamientos().isEmpty()){
                int indice = (int)Math.floor (Math.random() * principalControlador.getBookYourStay().getAlojamientos().size());
                alojamientoRandom = principalControlador.getBookYourStay().getAlojamientos().get(indice);
                principalControlador.getSesion().setAlojamientoAleatorio(alojamientoRandom);
                System.out.println("Alojamiento random: " + alojamientoRandom);
                nombre.setText(alojamientoRandom.getNombre());
                ciudad.setText(String.valueOf(alojamientoRandom.getTipoCiudad()));
                precio.setText(String.valueOf(alojamientoRandom.getValorNoche()));
                capacidad.setText(String.valueOf(alojamientoRandom.getCapacidadMaxima()));
                imagen.setImage(new javafx.scene.image.Image((alojamientoRandom.getImagen())));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void verDetallesAlojamiento() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        alojamientoRandom();

        try {
            ObservableList<Alojamiento> alojamientosConOferta = principalControlador.getBookYourStay().listarOfertasEspeciales();

            // Configurar las columnas de la tabla
            colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
            colCiudad.setCellValueFactory(celldata -> new SimpleStringProperty(celldata.getValue().getTipoCiudad().toString()));
            colTipoAlojamiento.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTipoAlojamiento().toString()));

            tablaOfertas.setItems(alojamientosConOferta);
        } catch (Exception e) {
            e.printStackTrace();
            principalControlador.mostrarAlerta("Error al cargar las ofertas especiales.", Alert.AlertType.ERROR);
        }
    }

    public void inicializarObservable(Observable observable) {
        this.observable = observable;
    }

    @Override
    public void notificar() {

    }
}
