package co.edu.uniquindio.bookyourstay.controlador;

import co.edu.uniquindio.bookyourstay.modelo.*;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoAlojamiento;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoCiudad;
import co.edu.uniquindio.bookyourstay.servicio.CreacionAlojamiento;
import co.edu.uniquindio.bookyourstay.servicio.ServiciosEmpresa;
import jakarta.mail.Session;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PrincipalControlador implements ServiciosEmpresa {

    @Getter
    private final BookYourStay bookYourStay;

    public PrincipalControlador() {
        bookYourStay = new BookYourStay();
    }

    @Override
    public void cargarDatosEmpresa() throws Exception {
    }

    @Override
    public void guardarDatosEmpresa() throws Exception {
    }

    @Override
    public Cliente registrarCliente(String cedula, String nombre, String telefono, String email, String password) throws Exception {
        return null;
    }

    @Override
    public Cliente iniciarSesion(String email, String password) throws Exception {
        return null;
    }

    @Override
    public boolean eliminarCuentaCliente(String cedulaCliente) throws Exception {
        return false;
    }

    @Override
    public Cliente obtenerCliente(String cedula) throws Exception {
        return null;
    }

    @Override
    public Cliente validarUsuario(String email, String password) throws Exception {
        return null;
    }

    @Override
    public Cliente obtenerUsuario(String email) throws Exception {
        return null;
    }

    @Override
    public boolean activarUsuario(String codigoActivacion, Cliente cliente) throws Exception {
        return false;
    }

    @Override
    public void enviarCodigoActivacion(Cliente cliente) throws Exception {

    }

    @Override
    public Cliente editarCuenta(String cedula, String nombre, String telefono, String email, String password) throws Exception {
        return null;
    }

    @Override
    public boolean validarIngresoAdministrador(String email, String password) throws Exception {
        return false;
    }

    @Override
    public Alojamiento crearAlojamiento(String nombre, String descripcion, String imagen, LocalDate fechaEstancia, float valorNoche, int numHuespedes, List<String> serviciosIncluidos, TipoAlojamiento tipoAlojamiento, TipoCiudad tipoCiudad) throws Exception {
        return null;
    }

    @Override
    public CreacionAlojamiento crearAlojamiento(TipoAlojamiento tipoAlojamiento) throws Exception {
        return null;
    }

    @Override
    public Alojamiento obtenerAlojamiento(String codigo) throws Exception {
        return null;
    }

    @Override
    public Alojamiento actualizarAlojamiento(Alojamiento alojamiento) throws Exception {
        return null;
    }

    @Override
    public ArrayList<Alojamiento> listarAlojamientos() throws Exception {
        return null;
    }

    @Override
    public ArrayList<Alojamiento> listarAlojamientos(TipoAlojamiento tipoAlejamiento) throws Exception {
        return null;
    }

    @Override
    public ArrayList<Alojamiento> listarAlojamientos(TipoCiudad tipoCiudad) throws Exception {
        return null;
    }

    @Override
    public ArrayList<Alojamiento> listarAlojamientos(String nombreAlojamiento) throws Exception {
        return null;
    }

    @Override
    public ArrayList<Cliente> listarClientes() throws Exception {
        return null;
    }

    @Override
    public List<Reserva> listarReservasCliente(String cedulaCliente) throws Exception {
        return null;
    }

    @Override
    public Reserva realizarReserva(Cliente cliente, Alojamiento alojamiento, LocalDate fechaInicio, LocalDate fechaFin, int numHuespedes, Factura factura) throws Exception {
        return null;
    }

    @Override
    public boolean cancelarReserva(Reserva reserva) throws Exception {
        return false;
    }

    @Override
    public ArrayList<Reserva> listarReservas() throws Exception {
        return null;
    }

    @Override
    public float calcularCostoReserva(Reserva reserva) throws Exception {
        return 0;
    }

    @Override
    public void recargarBilleteraVirtual(Cliente cliente, float monto) throws Exception {

    }

    @Override
    public String agregarResena(Reserva reserva, String comentario, int calificacion) throws Exception {
        return null;
    }

    @Override
    public Cliente cambiarPasswordC(String cedula, String nuevaPassword) throws Exception {
        return null;
    }

    @Override
    public void enviarNotificacion(String destinatario, String asunto, String mensaje) throws Exception {

    }

    @Override
    public boolean eliminarAlojamiento(Alojamiento alojamiento) throws Exception {
        return false;
    }

    @Override
    public float aplicarDescuentos(Alojamiento alojamiento, float porcentaje) throws Exception {
        return 0;
    }

    @Override
    public float crearTarifaDescuento(Alojamiento alojamiento, LocalDate fechaInicio, LocalDate fechaFin, float descuento) throws Exception {
        return 0;
    }

    @Override
    public int verEstadisticas(String ciudad) throws Exception {
        return 0;
    }

    @Override
    public ArrayList<Alojamiento> listaPopularesPorCiudad(String ciudad) throws Exception {
        return null;
    }

    @Override
    public ArrayList<Reserva> listaMasRentables(int limite) throws Exception {
        return null;
    }

    @Override
    public Administrador cambiarPassword(String email, String nuevaPassword) throws Exception {
        return null;
    }

    @Override
    public Factura generarFactura(Reserva reserva) throws Exception {
        return null;
    }

    @Override
    public String enviarCodigoQR(Factura factura, String emailCliente) throws Exception {
        return null;
    }

    @Override
    public void crearOfertaEspecial(Alojamiento alojamiento, LocalDate fechaInicio, LocalDate fechaFin, float descuento) throws Exception {

    }

    @Override
    public List<Alojamiento> listarOfertasEspeciales() throws Exception {
        return null;
    }
}
