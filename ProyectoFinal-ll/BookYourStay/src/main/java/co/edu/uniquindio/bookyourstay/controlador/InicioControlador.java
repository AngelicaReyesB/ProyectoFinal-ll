package co.edu.uniquindio.bookyourstay.controlador;

import co.edu.uniquindio.bookyourstay.controlador.observador.Observable;
import co.edu.uniquindio.bookyourstay.modelo.Alojamiento;
import co.edu.uniquindio.bookyourstay.modelo.Cliente;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class InicioControlador implements Observable, Initializable {

    @FXML private Button btnIngresar;
    @FXML private Button btnVer;
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
    private Alojamiento alojamientoRandom;
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
        // Corregimos la condición añadiendo el paréntesis que falta
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
            if (!principalControlador.getBookYourStay().getAlojamientos().isEmpty()) {
                // Seleccionar un alojamiento aleatorio
                int indice = (int) Math.floor(Math.random() * principalControlador.getBookYourStay().getAlojamientos().size());
                alojamientoRandom = principalControlador.getBookYourStay().getAlojamientos().get(indice);
                colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
                colCiudad.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTipoCiudad().toString()));
                colPrecio.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getValorNoche())));

                colImagen.setCellValueFactory(cellData -> {
                    ImageView imageView = new ImageView(cellData.getValue().getImagen()); // Ajustar según tipo de dato de imagen
                    imageView.setFitWidth(400); // Ajustar tamaño
                    imageView.setFitHeight(400);
                    return new SimpleObjectProperty<>(imageView);
                });

                // Mostrar el alojamiento aleatorio en la tabla
                tablaAlojamientosAleatoria.getItems().clear();
                tablaAlojamientosAleatoria.getItems().add(alojamientoRandom);

                System.out.println("ALOJAMIENTO RANDOM: " + alojamientoRandom);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void verDetallesAlojamiento() {
        // Aquí va la lógica para ver detalles del alojamiento
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        alojamientoRandom();
    }

    @Override
    public void notificar() {
        // Implementación del patrón Observer
    }
}
