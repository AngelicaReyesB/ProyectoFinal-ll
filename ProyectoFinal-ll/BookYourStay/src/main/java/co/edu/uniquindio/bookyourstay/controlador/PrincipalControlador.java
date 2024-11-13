package co.edu.uniquindio.bookyourstay.controlador;

import co.edu.uniquindio.bookyourstay.modelo.*;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoAlojamiento;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoCiudad;
import co.edu.uniquindio.bookyourstay.servicio.CreacionAlojamiento;
import co.edu.uniquindio.bookyourstay.servicio.ServiciosEmpresa;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PrincipalControlador implements ServiciosEmpresa {

    @Getter
    private final BookYourStay bookYourStay;
    @Getter
    private final Sesion sesion;
    private static PrincipalControlador INSTANCIA;

    public PrincipalControlador() {
        bookYourStay = new BookYourStay();
        sesion = new Sesion();
        inicializarValores();
    }

    public static PrincipalControlador getInstancia(){
        if(INSTANCIA == null){
            INSTANCIA =new PrincipalControlador();
        }
        return INSTANCIA;
    }

    private void inicializarValores(){
        try {
            sesion.setCliente(bookYourStay.listarClientes());
            sesion.setAlojamientos(bookYourStay.listarAlojamientos());
        } catch (Exception e) {
            System.out.println("Error al inicializar el valor de Sesión");
        }
    }

    public FXMLLoader navegarVentana(String nombreArchivoFxml, String tituloVentana){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(nombreArchivoFxml));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle(tituloVentana);
            stage.setMaximized(true);
            stage.show();
            return loader;

        }catch (Exception e){
            e.printStackTrace();
            return  null;
        }

    }

    public void mostrarAlerta(String mensaje, Alert.AlertType tipo){
        Alert alert = new Alert(tipo);
        alert.setHeaderText(mensaje);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public ButtonType mostrarAlertaConfirmacion(String mensaje, Alert.AlertType tipo){
        Alert alert = new Alert(tipo);
        alert.setHeaderText(mensaje);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
        return alert.getResult();
    }

    public void cerrarVentana(Node node){
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }
    @Override
    public void cargarDatosEmpresa() throws Exception {
    }

    @Override
    public void guardarDatosEmpresa() throws Exception {
    }

    @Override
    public Cliente registrarCliente(String cedula, String nombre, String telefono, String email, String password) throws Exception {
        return bookYourStay.registrarCliente(cedula, nombre, telefono, email, password);
    }

    @Override
    public Cliente iniciarSesion(String email, String password) throws Exception {
        return bookYourStay.iniciarSesion(email, password);
    }

    @Override
    public boolean eliminarCuentaCliente(String cedulaCliente) throws Exception {
        return bookYourStay.eliminarCuentaCliente(cedulaCliente);
    }

    @Override
    public Cliente obtenerCliente(String cedula) throws Exception {
        return bookYourStay.obtenerCliente(cedula);
    }

    @Override
    public Cliente validarUsuario(String email, String password) throws Exception {
        return bookYourStay.validarUsuario(email, password);
    }

    @Override
    public Cliente obtenerUsuario(String email) throws Exception {
        return obtenerUsuario(email);
    }

    @Override
    public boolean activarUsuario(String codigoActivacion, Cliente cliente) throws Exception {
        return bookYourStay.activarUsuario(codigoActivacion, cliente);
    }

    @Override
    public void enviarCodigoActivacion(Cliente cliente) throws Exception {
        bookYourStay.enviarCodigoActivacion(cliente);
    }

    @Override
    public Cliente editarCuenta(String cedula, String nombre, String telefono, String email, String password) throws Exception {
        return bookYourStay.editarCuenta(cedula, nombre, telefono, email, password);
    }

    @Override
    public boolean validarIngresoAdministrador(String email, String password) throws Exception {
        return bookYourStay.validarIngresoAdministrador(email, password);
    }

    @Override
    public Alojamiento crearAlojamiento(String nombre, String descripcion, String imagen, LocalDate fechaEstancia, float valorNoche, int numHuespedes, List<String> serviciosIncluidos, TipoAlojamiento tipoAlojamiento, TipoCiudad tipoCiudad) throws Exception {
        return bookYourStay.crearAlojamiento(nombre, descripcion, imagen, fechaEstancia, valorNoche, numHuespedes, serviciosIncluidos, tipoAlojamiento, tipoCiudad);
    }

    @Override
    public CreacionAlojamiento crearAlojamiento(TipoAlojamiento tipoAlojamiento) throws Exception {
        return bookYourStay.crearAlojamiento(tipoAlojamiento);
    }

    @Override
    public Alojamiento obtenerAlojamiento(String codigo) throws Exception {
        return obtenerAlojamiento(codigo);
    }

    @Override
    public Alojamiento actualizarAlojamiento(Alojamiento alojamiento) throws Exception {
        return actualizarAlojamiento(alojamiento);
    }

    @Override
    public ArrayList<Alojamiento> listarAlojamientos() throws Exception {
        return bookYourStay.listarAlojamientos();
    }

    @Override
    public ArrayList<Alojamiento> listarAlojamientos(TipoAlojamiento tipoAlejamiento) throws Exception {
        return bookYourStay.listarAlojamientos(tipoAlejamiento);
    }

    @Override
    public ArrayList<Alojamiento> listarAlojamientos(TipoCiudad tipoCiudad) throws Exception {
        return bookYourStay.listarAlojamientos(tipoCiudad);
    }

    @Override
    public ArrayList<Alojamiento> listarAlojamientos(String nombreAlojamiento) throws Exception {
        return bookYourStay.listarAlojamientos(nombreAlojamiento);
    }

    @Override
    public Cliente listarClientes() throws Exception {
        return bookYourStay.listarClientes();
    }

    @Override
    public List<Reserva> listarReservasCliente(String cedulaCliente) throws Exception {
        return bookYourStay.listarReservasCliente(cedulaCliente);
    }

    @Override
    public Reserva realizarReserva(Cliente cliente, Alojamiento alojamiento, LocalDate fechaInicio, LocalDate fechaFin, int numHuespedes, Factura factura) throws Exception {
        return bookYourStay.realizarReserva(cliente, alojamiento, fechaInicio, fechaFin, numHuespedes, factura);
    }

    @Override
    public boolean cancelarReserva(Reserva reserva) throws Exception {
        return bookYourStay.cancelarReserva(reserva);
    }

    @Override
    public ArrayList<Reserva> listarReservas() throws Exception {
        return bookYourStay.listarReservas();
    }

    @Override
    public float calcularCostoReserva(Reserva reserva) throws Exception {
        return bookYourStay.calcularCostoReserva(reserva);
    }

    @Override
    public void recargarBilleteraVirtual(Cliente cliente, float monto) throws Exception {
        bookYourStay.recargarBilleteraVirtual(cliente, monto);
    }

    @Override
    public String agregarResena(Reserva reserva, String comentario, int calificacion) throws Exception {
        return bookYourStay.agregarResena(reserva, comentario, calificacion);
    }

    @Override
    public Cliente cambiarPasswordC(String cedula, String nuevaPassword) throws Exception {
        return bookYourStay.cambiarPasswordC(cedula, nuevaPassword);
    }

    @Override
    public void enviarNotificacion(String destinatario, String asunto, String mensaje) throws Exception {
        bookYourStay.enviarNotificacion(destinatario, asunto, mensaje);
    }

    @Override
    public boolean eliminarAlojamiento(Alojamiento alojamiento) throws Exception {
        return bookYourStay.eliminarAlojamiento(alojamiento);
    }

    @Override
    public float aplicarDescuentos(Alojamiento alojamiento, float porcentaje) throws Exception {
        return bookYourStay.aplicarDescuentos(alojamiento, porcentaje);
    }

    @Override
    public float crearTarifaDescuento(Alojamiento alojamiento, LocalDate fechaInicio, LocalDate fechaFin, float descuento) throws Exception {
        return bookYourStay.crearTarifaDescuento(alojamiento, fechaInicio, fechaFin, descuento);
    }

    @Override
    public int verEstadisticas(String ciudad) throws Exception {
        return bookYourStay.verEstadisticas(ciudad);
    }

    @Override
    public ArrayList<Alojamiento> listaPopularesPorCiudad(String ciudad) throws Exception {
        return bookYourStay.listaPopularesPorCiudad(ciudad);
    }

    @Override
    public ArrayList<Reserva> listaMasRentables(int limite) throws Exception {
        return bookYourStay.listaMasRentables(limite);
    }

    @Override
    public Administrador cambiarPassword(String email, String nuevaPassword) throws Exception {
        return bookYourStay.cambiarPassword(email, nuevaPassword);
    }

    @Override
    public Factura generarFactura(Reserva reserva) throws Exception {
        return bookYourStay.generarFactura(reserva);
    }

    @Override
    public String enviarCodigoQR(Factura factura, String emailCliente) throws Exception {
        return bookYourStay.enviarCodigoQR(factura, emailCliente);
    }

    @Override
    public void crearOfertaEspecial(Alojamiento alojamiento, LocalDate fechaInicio, LocalDate fechaFin, float descuento) throws Exception {
        bookYourStay.crearOfertaEspecial(alojamiento, fechaInicio, fechaFin, descuento);
    }

    @Override
    public List<Alojamiento> listarOfertasEspeciales() throws Exception {
        return bookYourStay.listarOfertasEspeciales();
    }
}
