package co.edu.uniquindio.bookyourstay.servicio;

import co.edu.uniquindio.bookyourstay.controlador.observador.Observable;
import co.edu.uniquindio.bookyourstay.modelo.*;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoAlojamiento;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoCiudad;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public interface ServiciosEmpresa {
   // void cargarDatosEmpresa() throws Exception;
    //void guardarDatosEmpresa() throws Exception;
    Cliente registrarCliente(String cedula, String nombre, String telefono, String email, String password) throws Exception;
    //Cliente iniciarSesion(String email, String password) throws Exception;
    boolean eliminarCuentaCliente(String cedulaCliente) throws  Exception;
    Cliente obtenerCliente(String cedula) throws Exception;
    Cliente validarUsuario(String email, String password) throws Exception;
    Cliente obtenerUsuario(String email) throws Exception;
    boolean activarUsuario(String codigoActivacion, Cliente cliente) throws Exception;
    void enviarCodigoActivacion(Cliente cliente) throws Exception;
    void obtenerAdministrador(String email) throws Exception;
    void enviarCorreoRecuperacion(String email) throws Exception;
    String generarCodigoVerificacion();
    void editarCuenta(String cedula, String nombre, String telefono, String email, String password) throws Exception;
    boolean validarCodigoActivacion(String email, String codigo) throws Exception;
    boolean validarIngresoAdministrador(String email, String password) throws Exception;
    CreacionAlojamiento crearAlojamiento(TipoAlojamiento tipoAlojamiento) throws Exception;
    Alojamiento crearAlojamiento(String nombre, String descripcion, String imagen, LocalDate fechaEstancia, float valorNoche, int numHuespedes, List<String> serviciosIncluidos, TipoAlojamiento tipoAlojamiento, TipoCiudad tipoCiudad, boolean activo) throws Exception;
    Alojamiento obtenerAlojamiento(String codigo) throws Exception;
    Alojamiento actualizarAlojamiento(Alojamiento alojamiento) throws Exception;
    //void actualizarAlojamiento(Reserva reserva) throws Exception;
    ArrayList<Alojamiento> listarAlojamientos() throws Exception;
    ArrayList<Alojamiento> listarAlojamientos(TipoAlojamiento tipoAlejamiento) throws Exception;
    ArrayList<Alojamiento> listarAlojamientos(TipoCiudad tipoCiudad) throws Exception;
    ArrayList<Alojamiento> listarAlojamientos(String nombreAlojamiento) throws Exception;
    ArrayList<Cliente> listarClientes() throws Exception;
    List<Reserva> listarReservasCliente(String cedulaCliente) throws Exception;
    //List<Alojamiento> buscarAlojamiento(String ciudad, TipoAlojamiento tipoAlejamiento, float precioMaximo) throws Exception;
    Reserva realizarReserva(Cliente cliente, Alojamiento alojamiento, LocalDate fechaInicio, LocalDate fechaFin, int numHuespedes, Factura factura) throws Exception;
    boolean cancelarReserva(Reserva reserva) throws Exception;
    ArrayList<Reserva> listarReservas() throws Exception;
    float calcularCostoReserva(Reserva reserva) throws Exception;
    void editarOferta(Alojamiento alojamiento, LocalDate nuevaFechaInicio, LocalDate nuevaFechaFin, float nuevoDescuento) throws Exception;
    boolean eliminarOferta(String nombreAlojamiento) throws Exception;
    void recargarBilleteraVirtual(Cliente cliente, float monto) throws Exception;
    String agregarResena(Reserva reserva, String comentario, int calificacion) throws Exception;
    Cliente cambiarPasswordC(String cedula, String nuevaPassword, String codigoActivacion) throws Exception;
    void enviarNotificacion(String destinatario, String asunto, String mensaje) throws Exception;
    boolean eliminarAlojamiento(Alojamiento alojamiento) throws Exception;
    float aplicarDescuentos(Alojamiento alojamiento, float porcentaje) throws Exception;
    float crearTarifaDescuento(Alojamiento alojamiento, LocalDate fechaInicio, LocalDate fechaFin, float descuento) throws Exception;
    int verEstadisticas(String ciudad) throws Exception;
    ArrayList<Alojamiento> listaPopularesPorCiudad(String ciudad) throws Exception;
    ArrayList<Reserva> listaMasRentables(int limite) throws Exception;
    Administrador cambiarPassword(String email, String nuevaPassword) throws Exception;
    Factura generarFactura(Reserva reserva) throws Exception;
    void crearOfertaEspecial(Alojamiento alojamiento, LocalDate fechaInicio, LocalDate fechaFin, float descuento) throws Exception;
    ObservableList<Alojamiento> listarOfertasEspeciales() throws Exception;
    String generarCodigoQR(Factura factura) throws Exception;
    boolean verificarCodigoActivacion(String cedula, String codigoActivacion) throws Exception;

 void enviarFacturaQR(Reserva reserva) throws Exception;
}
