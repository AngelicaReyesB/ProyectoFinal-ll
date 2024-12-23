package co.edu.uniquindio.bookyourstay.controlador;

import co.edu.uniquindio.bookyourstay.controlador.observador.Observable;
import co.edu.uniquindio.bookyourstay.modelo.*;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoAlojamiento;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoCiudad;
import co.edu.uniquindio.bookyourstay.servicio.CreacionAlojamiento;
import co.edu.uniquindio.bookyourstay.servicio.ServiciosEmpresa;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public class PrincipalControlador implements ServiciosEmpresa {

    private final BookYourStay bookYourStay;
    private final Sesion sesion;
    private static PrincipalControlador INSTANCIA;
    private List<Observable> observables;

    public PrincipalControlador() {
        bookYourStay = new BookYourStay();
        sesion = new Sesion();
        observables = new ArrayList<>();
        inicializarValores();
    }

    public static PrincipalControlador getInstancia() {
        if (INSTANCIA == null) {
            INSTANCIA = new PrincipalControlador();
        }
        return INSTANCIA;
    }

    private void inicializarValores() {
        try {
            sesion.setClientes(bookYourStay.listarClientes());
            sesion.setAlojamientos(bookYourStay.listarAlojamientos());
        } catch (Exception e) {
            System.out.println("Error al inicializar el valor de Sesión");
        }
    }

    public void registrarObservador(Observable observador) {
        if (!observables.contains(observador)) {
            observables.add(observador);
        }
    }

    // Método para notificar a todos los observadores
    public void notificarObservadores() {
        for (Observable observador : observables) {
            observador.notificar();
        }
    }

    public FXMLLoader navegarVentana(String nombreArchivoFxml, String tituloVentana){
        try {
            // Cargar la vista
            FXMLLoader loader = new FXMLLoader(getClass().getResource(nombreArchivoFxml));
            Parent root = loader.load();

            // Crear la escena
            Scene scene = new Scene(root);

            // Crear un nuevo escenario (ventana)
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle(tituloVentana);
            stage.setMaximized(true);
            // Mostrar la nueva ventana
            stage.show();
            return loader;

        }catch (Exception e){
            e.printStackTrace();
            return  null;
        }

    }

    public void mostrarAlerta(String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setHeaderText(mensaje);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public void cerrarVentana(Node node) {
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }



    @Override
    public Cliente registrarCliente(String cedula, String nombre, String telefono, String email, String password) throws Exception {
        return bookYourStay.registrarCliente(cedula, nombre, telefono, email, password);
    }

    //@Override
    //    public Cliente iniciarSesion(String email, String password) throws Exception {
    //        return bookYourStay.iniciarSesion(email, password);
    //    }

    @Override
    public boolean eliminarCuentaCliente(String cedulaCliente) throws Exception {
        return bookYourStay.eliminarCuentaCliente(cedulaCliente);
    }

    @Override
    public Cliente obtenerCliente(String cedula) throws Exception {
        return bookYourStay.obtenerCliente(cedula);
    }

    @Override
    public Cliente validarUsuario(String cedula, String password) throws Exception {
        return bookYourStay.validarUsuario(cedula, password);
    }

    @Override
    public Cliente obtenerUsuario(String email) throws Exception {
        return bookYourStay.obtenerUsuario(email);
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
    public void obtenerAdministrador(String email) throws Exception {
        bookYourStay.obtenerAdministrador(email);
    }

    @Override
    public void enviarCorreoRecuperacion(String email) throws Exception {
        bookYourStay.enviarCorreoRecuperacion(email);
    }

    @Override
    public String generarCodigoVerificacion() {
        return bookYourStay.generarCodigoVerificacion();
    }

    @Override
    public void editarCuenta(String cedula, String nombre, String telefono, String email, String password) throws Exception {
        bookYourStay.editarCuenta(cedula, nombre, telefono, email, password);
    }

    @Override
    public boolean validarIngresoAdministrador(String email, String password) throws Exception {
        return bookYourStay.validarIngresoAdministrador(email, password);
    }

    @Override
    public Alojamiento crearAlojamiento(String nombre, String descripcion, String imagen, float valorNoche, int numHuespedes, List<String> serviciosIncluidos, TipoAlojamiento tipoAlojamiento, TipoCiudad tipoCiudad, boolean activo) throws Exception {
        return bookYourStay.crearAlojamiento(nombre, descripcion, imagen, valorNoche, numHuespedes, serviciosIncluidos, tipoAlojamiento, tipoCiudad, activo);
    }

    @Override
    public CreacionAlojamiento crearAlojamiento(TipoAlojamiento tipoAlojamiento) throws Exception {
        return bookYourStay.crearAlojamiento(tipoAlojamiento);
    }

    @Override
    public Alojamiento obtenerAlojamiento(String codigo) throws Exception {
        return bookYourStay.obtenerAlojamiento(codigo);
    }

    @Override
    public Alojamiento actualizarAlojamiento(Alojamiento alojamiento) throws Exception {
        return bookYourStay.actualizarAlojamiento(alojamiento);
    }

    @Override
    public ArrayList<Alojamiento> listarAlojamientos() {
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
    public ArrayList<Cliente> listarClientes() throws Exception {
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
    public void editarOferta(Alojamiento alojamiento, LocalDate nuevaFechaInicio, LocalDate nuevaFechaFin, float nuevoDescuento) throws Exception {
        bookYourStay.editarOferta(alojamiento, nuevaFechaInicio, nuevaFechaFin, nuevoDescuento);
    }

    @Override
    public boolean eliminarOferta(String nombreAlojamiento) throws Exception {
        return bookYourStay.eliminarOferta(nombreAlojamiento);
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
    public int verEstadisticas(String ciudad) throws Exception {
        return bookYourStay.verEstadisticas(ciudad);
    }

    @Override
    public List<Alojamiento> listarAlojamientosDisponibles(List<Alojamiento> alojamientos, TipoCiudad tipoCiudad, TipoAlojamiento tipoAlojamiento, int capacidadMinima, float precioMaximo) {
        return bookYourStay.listarAlojamientosDisponibles(alojamientos, tipoCiudad, tipoAlojamiento, capacidadMinima, precioMaximo);
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
    public Factura generarFactura(Reserva reserva) throws Exception {
        return bookYourStay.generarFactura(reserva);
    }

    @Override
    public void crearOfertaEspecial(Alojamiento alojamiento, LocalDate fechaInicio, LocalDate fechaFin, float descuento) throws Exception {
        bookYourStay.crearOfertaEspecial(alojamiento, fechaInicio, fechaFin, descuento);
    }

    @Override
    public ObservableList<Alojamiento> listarOfertasEspeciales() throws Exception {
        return bookYourStay.listarOfertasEspeciales();
    }

    @Override
    public Administrador cambiarPassword(String email, String nuevaPassword) throws Exception{
        return bookYourStay.cambiarPassword(email,nuevaPassword);
    }

    @Override
    public Cliente cambiarPasswordC(String cedula, String nuevaPassword, String codigoActivacion) throws Exception {
        return bookYourStay.cambiarPasswordC(cedula, nuevaPassword,codigoActivacion);
    }

    @Override
    public String generarCodigoQR(Factura factura) throws Exception {
        return bookYourStay.generarCodigoQR(factura);
    }

    @Override
    public boolean verificarCodigoActivacion(String cedula, String codigoActivacion) throws Exception {
        return bookYourStay.verificarCodigoActivacion(cedula,codigoActivacion);
    }

    @Override
    public String enviarFacturaQR(Reserva reserva) throws Exception {
        return bookYourStay.enviarFacturaQR(reserva);
    }

    @Override
    public boolean validarCodigoVerificacion(String codigoIngresado) throws Exception {
        return bookYourStay.validarCodigoVerificacion(codigoIngresado);
    }

    @Override
    public String verificarSaldoDisponible(Cliente cliente, double total) throws Exception {
        return bookYourStay.verificarSaldoDisponible(cliente,total);
    }

    @Override
    public boolean esCorreoValido(String email) {
        return bookYourStay.esCorreoValido(email);
    }

}
