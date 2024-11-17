package co.edu.uniquindio.bookyourstay.controlador;

import co.edu.uniquindio.bookyourstay.controlador.observador.Observable;
import co.edu.uniquindio.bookyourstay.modelo.Alojamiento;
import co.edu.uniquindio.bookyourstay.modelo.Cliente;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
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

    private void alojamientosAleatorios() {
        try {
            if (!principalControlador.getBookYourStay().getAlojamientos().isEmpty()) {
                // Obtener la lista de alojamientos
                List<Alojamiento> alojamientos = principalControlador.getBookYourStay().getAlojamientos();

                // Seleccionar dos índices aleatorios sin repetir
                int indice1 = (int) Math.floor(Math.random() * alojamientos.size());
                int indice2;
                do {
                    indice2 = (int) Math.floor(Math.random() * alojamientos.size());
                } while (indice1 == indice2); // Asegurar que los índices sean diferentes

                // Obtener los alojamientos seleccionados
                Alojamiento alojamiento1 = alojamientos.get(indice1);
                Alojamiento alojamiento2 = alojamientos.get(indice2);

                // Configurar las columnas de la tabla
                colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
                colCiudad.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTipoCiudad().toString()));
                colPrecio.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getValorNoche())));
                colImagen.setCellValueFactory(cellData -> {
                    ImageView imageView = new ImageView(cellData.getValue().getImagen());
                    imageView.setFitWidth(200);
                    imageView.setFitHeight(200);
                    return new SimpleObjectProperty<>(imageView);
                });

                // Mostrar los dos alojamientos aleatorios en la tabla
                tablaAlojamientosAleatoria.getItems().clear();
                tablaAlojamientosAleatoria.getItems().addAll(alojamiento1, alojamiento2);

                System.out.println("ALOJAMIENTOS RANDOM: " + alojamiento1 + ", " + alojamiento2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //private void mostrarOfertasAlojamientos() {
    //        try {
    //            // Obtener los alojamientos con ofertas especiales
    //            ObservableList<Alojamiento> alojamientosConOfertas = principalControlador.getBookYourStay().listarOfertasEspeciales();
    //
    //            if (!alojamientosConOfertas.isEmpty()) {
    //                // Configurar la columna de imagen de ofertas
    //                colImagenOfertas.setCellValueFactory(cellData -> {
    //                    ImageView imageView = new ImageView(cellData.getValue().getImagen()); // Usamos el campo imagen
    //                    imageView.setFitWidth(200); // Ajusta el tamaño
    //                    imageView.setFitHeight(200);
    //                    return new SimpleObjectProperty<>(imageView);
    //                });
    //
    //                // Configurar otras columnas (si es necesario)
    //                colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
    //                colCiudad.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTipoCiudad().toString()));
    //                colPrecio.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getValorNoche())));
    //
    //                // Llenar la tabla con los datos
    //                tablaOfertasAlojamientos.getItems().clear();
    //                tablaOfertasAlojamientos.setItems(alojamientosConOfertas);
    //
    //                System.out.println("ALOJAMIENTOS CON OFERTAS: " + alojamientosConOfertas);
    //            } else {
    //                System.out.println("No hay alojamientos con ofertas especiales disponibles.");
    //            }
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //        }
    //    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        alojamientosAleatorios();
        //mostrarOfertasAlojamientos(); // Llamamos al método para mostrar las ofertas
    }



    @Override
    public void notificar() {

    }

    public void verDetallesAlojamiento(ActionEvent actionEvent) {
    }
}
