package co.edu.uniquindio.bookyourstay.modelo;

import co.edu.uniquindio.bookyourstay.factory.AlojamientoApartamento;
import co.edu.uniquindio.bookyourstay.factory.AlojamientoCasa;
import co.edu.uniquindio.bookyourstay.factory.AlojamientoHotel;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoAlojamiento;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoCiudad;
import co.edu.uniquindio.bookyourstay.servicio.CreacionAlojamiento;
import co.edu.uniquindio.bookyourstay.servicio.ServiciosEmpresa;
import co.edu.uniquindio.bookyourstay.utils.EnvioEmail;
import co.edu.uniquindio.bookyourstay.utils.Persistencia;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;

import java.nio.file.FileSystems;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@Getter
@Setter
public class BookYourStay implements ServiciosEmpresa {
    private List<Cliente> clientes;
    private List<Alojamiento> alojamientos;
    private List<Factura> facturas;
    private List<Reserva> reservas;
    private Administrador administrador;
    Persistencia persistencia = new Persistencia();

    public BookYourStay(){
        try {
            clientes = new ArrayList<>();
            alojamientos = new ArrayList<>();
            facturas = new ArrayList<>();
            //cargarDatosEmpresa();
        }catch ( Exception e){
            System.out.println(e.getMessage());
        }
    }

    //@Override
    //    public void cargarDatosEmpresa() throws Exception {
    //        try {
    //            ArrayList<Cliente> clientesCargados = persistencia.cargarClientes();
    //            ArrayList<Alojamiento> alojamientosCargados = persistencia.cargarAlojamientos();
    //            ArrayList<Factura> facturasCargados = persistencia.cargarFacturas();
    //
    //            if (!clientesCargados.isEmpty()) {
    //                clientes.addAll(clientesCargados);
    //            }
    //            if (!alojamientosCargados.isEmpty()) {
    //                alojamientos.addAll(alojamientosCargados);
    //            }
    //            if (!facturasCargados.isEmpty()) {
    //                facturas.addAll(facturasCargados);
    //            }
    //        }catch (Exception e){
    //            throw new Exception(e.getMessage());
    //        }
    //    }
    //
    //    @Override
    //    public void guardarDatosEmpresa() throws Exception {
    //        try {
    //            persistencia.guardarAlojamientos(alojamientos);
    //            persistencia.guardarClientes(clientes);
    //            persistencia.guardarFacturas(facturas);
    //        }catch (Exception e){
    //            throw new Exception(e.getMessage());
    //        }
    //    }

    //si se hace uso en el controlador de registro cliente
    @Override
    public Cliente registrarCliente(String nombre, String cedula, String telefono, String email, String password) throws Exception {
        if(cedula.isEmpty() || cedula.isBlank() ){
            throw new Exception("El número de identificación es obligatorio");
        }

        if(obtenerCliente(cedula) != null){
            if(!obtenerCliente(cedula).getPassword().equals(password)){
                throw new Exception("Los datos de usuario y contraseña no coinciden");
            }
            throw new Exception("Ya existe un usuario con el número de identificación: "+cedula);
        }

        if(nombre.isEmpty() || nombre.isBlank()){
            throw new Exception("El nombre es obligatorio");
        }

        if(email.isEmpty() || email.isBlank()){
            throw new Exception("El correo electrónico es obligatorio");
        }

        if(password.isEmpty() || password.isBlank()){
            throw new Exception("La contraseña es obligatoria");
        }

        Cliente cliente;
        String codigoActivacion = UUID.randomUUID().toString();
        try{
            cliente = Cliente.builder()
                    .nombre(nombre)
                    .cedula(cedula)
                    .telefono(telefono)
                    .email(email)
                    .password(password)
                    .estadoCuenta(false)
                    .codigoActivacion(codigoActivacion)
                    .build();
            clientes.add(cliente);
            //guardarDatosEmpresa();
            System.out.println("Código de activación generado para el usuario " + nombre + ": " + codigoActivacion);

        } catch(Exception e){
            throw new Exception("No se puede agregar el nuevo cliente");
        }
        return cliente;
    }

    //se hace uso en inicio controlador
    @Override
    public Cliente obtenerCliente(String cedula) throws Exception {
        try {
            for (Cliente cliente : clientes) {
                System.out.println("CLIENTE QUE RECORRO EN BUSCAR: " + cliente);
                if (cliente.getCedula().equals(cedula)) {
                    // Verificamos si el cliente tiene una billetera virtual
                    if (cliente.getBilleteraVirtual() == null) {
                        cliente.setBilleteraVirtual(new BilleteraVirtual()); // Asignamos una billetera vacía
                    }
                    return cliente;
                }
            }
            return null; // Si no se encuentra el cliente
        } catch (Exception e) {
            throw new Exception("No se puede buscar cliente");
        }
    }


    //se hace uso en inicio controlador
    @Override
    public Cliente validarUsuario(String email, String password) throws Exception {
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            throw new Exception("Correo y contraseña no pueden estar vacíos.");
        }

        // Obtener el cliente mediante el correo
        Cliente cliente = obtenerCliente(email);
        if (cliente != null) {
            // Verificamos que la billetera virtual esté asociada
            if (cliente.getBilleteraVirtual() == null) {
                cliente.setBilleteraVirtual(new BilleteraVirtual()); // Asignamos una billetera vacía si no está asociada
            }

            // Validamos la contraseña
            if (cliente.getPassword().equals(password)) {
                return cliente;
            } else {
                throw new Exception("Los datos de ingreso son incorrectos.");
            }
        } else {
            throw new Exception("El usuario no existe.");
        }
    }

    //se hace uso en recuperar contraseña
    @Override
    public Cliente obtenerUsuario(String email) throws Exception {
        for (Cliente cliente : clientes) {
            if (cliente.getEmail().equals(email)) {
                String codigoVerificacion = generarCodigoVerificacion();
                System.out.println("Código de verificación para el usuario " + cliente.getNombre() + ": " + codigoVerificacion);
                String asunto = "Código de verificación - BookYourStay";
                String mensaje = """
                    Hola %s,
                    
                    Hemos recibido una solicitud para restablecer tu contraseña. 
                    Utiliza el siguiente código para completar el proceso:
                    
                    Código: %s
                    
                    Si no solicitaste este cambio, por favor ignora este correo.
                    
                    Saludos,
                    Equipo de BookYourStay
                    """.formatted(cliente.getNombre(), codigoVerificacion);
                EnvioEmail envioEmail = new EnvioEmail(email, asunto, mensaje);
                envioEmail.enviarNotificacion();
                return cliente;
            }
        }
        throw new Exception("Cliente no encontrado con el email proporcionado.");
    }

    //se hace uso en recuperación contraseña
    @Override
    public void obtenerAdministrador(String email) throws Exception {
        String usuarioAdministrador = "admin@bookyourstay.com";
        if (!email.equals(usuarioAdministrador)) {
            throw new Exception("Administrador no encontrado con el email proporcionado.");
        }
        enviarCorreoRecuperacion(email);
        System.out.println("Código de verificación para el usuario " + administrador.getEmail() + ": " + generarCodigoVerificacion());

    }

    //no se hace euso y debe de usarse en nueva contraseña
    @Override
    public void enviarCorreoRecuperacion(String email) throws Exception{
        String codigo = generarCodigoVerificacion();
        String mensaje = """
        Hola,
        
        Hemos recibido una solicitud para restablecer la contraseña de tu cuenta de administrador. 
        Si realizaste esta solicitud, utiliza el siguiente código de verificación:
        
        Código: """ + codigo + """
        
        Si no realizaste esta solicitud, ignora este correo.
        
        Saludos,
        Equipo de BookYourStay
        """;
        EnvioEmail envioEmail = new EnvioEmail(email, "Recuperación contraseña", mensaje);
        try {
            envioEmail.enviarNotificacion();
            throw new Exception("Correo de recuperación enviado correctamente.");
        } catch (Exception e) {
            throw new Exception("Error al enviar el correo de recuperación: " + e.getMessage());
        }
    }

    //no se hace uso y se hace uso en el metodo anterior
    @Override
    public String generarCodigoVerificacion() {
        return String.valueOf((int) (Math.random() * 900000) + 100000);
    }

    //se hace uso en actualizar datos clientes
    @Override
    public void editarCuenta(String cedula, String nombre, String telefono, String email, String password) throws Exception {

        if (cedula == null || cedula.isEmpty()) {
            throw new Exception("La cédula es obligatoria para editar la cuenta");
        }

        Cliente cliente = obtenerCliente(cedula);
        if (cliente == null) {
            throw new Exception("No se encontró un cliente con la cédula proporcionada");
        }

        if (nombre != null && !nombre.isEmpty()) {
            cliente.setNombre(nombre);
        }
        if (telefono != null && !telefono.isEmpty()) {
            cliente.setTelefono(telefono);
        }
        if (email != null && !email.isEmpty()) {
            cliente.setEmail(email);
        }
        if (password != null && !password.isEmpty()) {
            cliente.setPassword(password);
        }

        cliente.setCedula(cedula);
        cliente.setNombre(nombre);
        cliente.setTelefono(telefono);
        cliente.setEmail(email);
        cliente.setPassword(password);
        //guardarDatosEmpresa();
    }

    //si se hace uso en perfil controlador
    @Override
    public boolean eliminarCuentaCliente(String cedulaCliente) throws Exception {
        for (Cliente cliente : clientes) {
            if (cliente.getCedula().equals(cedulaCliente)) {
                clientes.remove(cliente);
                return true;
            }
        }
        throw new Exception("No se encontró un cliente con la cédula " + cedulaCliente);
    }


    @Override
    public ArrayList<Cliente> listarClientes() throws Exception {
        return new ArrayList<>(clientes);
    }

    //hace uso de otro método
    @Override
    public void enviarNotificacion(String destinatario, String asunto, String mensaje) throws Exception {
        try {
            EnvioEmail envioEmail = new EnvioEmail(destinatario, asunto, mensaje);
            envioEmail.enviarNotificacion();
        } catch (Exception e) {
            throw new Exception("No se puede enviar la notificación al correo electrónico", e);
        }
    }

    //se hace uso en el registro controlador
    @Override
    public void enviarCodigoActivacion(Cliente cliente) throws Exception {
        String email = cliente.getEmail();
        String mensajeActivacion = "Recuerde activar la cuenta para poder realizar sus reservas en BookYourStay. Código de activación: " +
                cliente.getCodigoActivacion();
        enviarNotificacion(email, "BookYourStay: Código de activación", mensajeActivacion );
    }

    //se hace uso en el controlador de activar cuenta
    @Override
    public boolean activarUsuario(String codigoActivacion, Cliente cliente) throws Exception {
        Cliente usuarioEncontrado = validarUsuario(cliente.getCedula(), cliente.getPassword());
        if(usuarioEncontrado != null){
            if(usuarioEncontrado.getCodigoActivacion().equals(codigoActivacion)){
                usuarioEncontrado.setEstadoCuenta(true);
                //guardarDatosEmpresa();
                return true;
            }else {
                throw new Exception("El código de activación no coincide");
            }
        }else {
            throw new Exception("El usuario no existe");
        }
    }

    //se hace uso en inicio controlador
    @Override
    public boolean validarIngresoAdministrador(String email, String password) throws Exception {
        String usuarioAdministrador = "admin";
        String passwordAdministrador = "admin123";
        return email.equals(usuarioAdministrador) && password.equals(passwordAdministrador);
    }

    //hace uso de otro método
    @Override
    public Alojamiento obtenerAlojamiento(String codigo) throws Exception {
        for (Alojamiento alojamiento: alojamientos){
            if(alojamiento.getCodigo().equals(codigo)){
                return alojamiento;
            }
        }
        return null;
    }

    //se hace uso en crear alojamiento
    @Override
    public Alojamiento actualizarAlojamiento(Alojamiento alojamiento) throws Exception {
        Alojamiento alojamientoEncontrado = obtenerAlojamiento(alojamiento.getCodigo());
        if (alojamientoEncontrado != null){
            alojamientoEncontrado.setCodigo(alojamiento.getCodigo());
            alojamientoEncontrado.setNombre(alojamiento.getNombre());
            alojamientoEncontrado.setDescripcion(alojamiento.getDescripcion());
            alojamientoEncontrado.setImagen(alojamiento.getImagen());
            alojamientoEncontrado.setTipoCiudad(alojamiento.getTipoCiudad());
            alojamientoEncontrado.setValorNoche(alojamiento.getValorNoche());
            alojamientoEncontrado.setCapacidadMaxima(alojamiento.getCapacidadMaxima());
            alojamientoEncontrado.setTipoAlojamiento(alojamiento.getTipoAlojamiento());
            alojamientoEncontrado.setServiciosIncluidos(alojamiento.getServiciosIncluidos());
            alojamientoEncontrado.setHabitaciones(alojamiento.getHabitaciones());
            alojamientoEncontrado.setCostoAseoMantenimiento(alojamiento.getCostoAseoMantenimiento());

            //guardarDatosEmpresa();
        }
        return alojamientoEncontrado;
    }

    //se hace uso en el controlador de búsquedas
    @Override
    public ArrayList<Alojamiento> listarAlojamientos() {
        ArrayList<Alojamiento> alojamientosActivos = new ArrayList<>();
        for (Alojamiento alojamiento: alojamientos){
            if(alojamiento.isActivo()){
                alojamientosActivos.add(alojamiento);
            }
        }
        return (ArrayList<Alojamiento>) alojamientos;
    }

    //se hace uso en el controlador de búsquedas
    @Override
    public ArrayList<Alojamiento> listarAlojamientos(TipoAlojamiento tipoAlojamiento) throws Exception {
        ArrayList<Alojamiento> alojamientosPorTipo = new ArrayList<>();
        for (Alojamiento alojamiento: alojamientos){
            if(alojamiento.getTipoAlojamiento() == tipoAlojamiento){
                alojamientosPorTipo.add(alojamiento);
            }
        }
        return alojamientosPorTipo;
    }

    //se hace uso en el controlador de búsquedas
    @Override
    public ArrayList<Alojamiento> listarAlojamientos(TipoCiudad tipoCiudad) throws Exception {
        ArrayList<Alojamiento> alojamientosPorCiudad = new ArrayList<>();
        for (Alojamiento alojamiento: alojamientos){
            if(alojamiento.getTipoCiudad() == tipoCiudad){
                alojamientosPorCiudad.add(alojamiento);
            }
        }
        return alojamientosPorCiudad;
    }

    // se hace uso en el controlador de crear ofertas
    @Override
    public ArrayList<Alojamiento> listarAlojamientos(String nombreAlojamiento) throws Exception {
        ArrayList<Alojamiento> alojamientosPorNombre = new ArrayList<>();
        for (Alojamiento alojamiento: alojamientos){
            if(alojamiento.getNombre().contains(nombreAlojamiento)){
                alojamientosPorNombre.add(alojamiento);
                return alojamientosPorNombre;
            }
        }
        return listarAlojamientos();
    }

    @Override
    public List<Alojamiento> listarAlojamientosDisponibles(List<Alojamiento> alojamientos, TipoCiudad tipoCiudad, TipoAlojamiento tipoAlojamiento, int capacidadMinima, float precioMaximo) {

        List<Alojamiento> alojamientosDisponibles = new ArrayList<>();

        for (Alojamiento alojamiento : alojamientos) {
            if (alojamiento.isActivo() &&
                    alojamiento.getTipoCiudad() == tipoCiudad &&
                    alojamiento.getTipoAlojamiento() == tipoAlojamiento &&
                    alojamiento.getCapacidadMaxima() >= capacidadMinima &&
                    alojamiento.getValorNoche() <= precioMaximo) {
                alojamientosDisponibles.add(alojamiento);
            }
        }

        return alojamientosDisponibles;
    }

    //no se hace uso y debe de usar en AlojamientosPopularesControlador
    @Override
    public ArrayList<Alojamiento> listaPopularesPorCiudad(String ciudad) throws Exception {
        ArrayList<Alojamiento> alojamientosPopulares = new ArrayList<>();

        TipoCiudad ciudadEnum = TipoCiudad.valueOf(ciudad.toUpperCase());

        for (Alojamiento alojamiento : alojamientos) {
            if (alojamiento.getTipoCiudad() == ciudadEnum) {
                alojamientosPopulares.add(alojamiento);
            }
        }
        alojamientosPopulares.sort((a1, a2) -> {
            int numReservasA1 = obtenerNumeroDeReservas(a1);
            int numReservasA2 = obtenerNumeroDeReservas(a2);
            return Integer.compare(numReservasA2, numReservasA1);
        });

        return alojamientosPopulares;
    }
    private int obtenerNumeroDeReservas(Alojamiento alojamiento) {
        int numeroDeReservas = 0;

        for (Reserva reserva : reservas) {
            if (reserva.getAlojamiento().equals(alojamiento)) {
                numeroDeReservas++;
            }
        }

        return numeroDeReservas;
    }

    //si se hace uso en crear oferta
    @Override
    public ObservableList<Alojamiento> listarOfertasEspeciales() throws Exception {
        List<Alojamiento> alojamientosConOferta = new ArrayList<>();

        // Filtrar los alojamientos con oferta especial
        for (Alojamiento alojamiento : alojamientos) {
            if (alojamiento.isOfertaEspecial()) {
                alojamientosConOferta.add(alojamiento);
            }
        }

        // Convertir a ObservableList antes de devolver
        return FXCollections.observableArrayList(alojamientosConOferta);
    }

    //se hace uso en el controlador de crear alojamientos
    @Override
    public boolean eliminarAlojamiento(Alojamiento alojamiento) throws Exception {
        try {
            if (alojamientos.contains(alojamiento)) {
                alojamientos.remove(alojamiento);
                return true;
            } else {
                throw new Exception("Alojamiento no encontrado.");
            }
        } catch (Exception e) {
            throw new Exception("Error al eliminar el alojamiento: " + e.getMessage());
        }
    }

    //se hace uso en el controlador de reserva
    @Override
    public Reserva realizarReserva(Cliente cliente, Alojamiento alojamiento, LocalDate fechaInicio, LocalDate fechaFin, int numHuespedes, Factura factura) throws Exception {
        if (cliente == null || alojamiento == null || fechaInicio == null || fechaFin == null || factura == null) {
            throw new IllegalArgumentException("Todos los campos deben estar completos.");
        }

        if (fechaFin.isBefore(fechaInicio) || fechaInicio.isBefore(LocalDate.now())) {
            throw new Exception("Las fechas de la reserva no son válidas.");
        }

        if (numHuespedes > alojamiento.getCapacidadMaxima()) {
            throw new Exception("El número de huéspedes excede la capacidad del alojamiento.");
        }

        if (!alojamiento.isActivo()) {
            throw new Exception("El alojamiento no está disponible para las fechas solicitadas.");
        }

        double totalReserva = factura.getTotal();
        if (cliente.getBilleteraVirtual().getMontoTotal() < totalReserva) {
            throw new Exception("Fondos insuficientes en la billetera virtual.");
        }

        Reserva nuevaReserva = Reserva.builder()
                .codigo(java.util.UUID.randomUUID().toString())
                .cliente(cliente)
                .alojamiento(alojamiento)
                .fechaInicio(fechaInicio)
                .fechaFin(fechaFin)
                .numHuespedes(numHuespedes)
                .pagado(true)
                .factura(factura)
                .codigoQR("Código QR generado") // Aquí puedes agregar lógica para generar un QR real
                .estadoReserva(true)
                .fechaCreacion(LocalDateTime.now())
                .build();

        cliente.getBilleteraVirtual().setMontoTotal((float) (cliente.getBilleteraVirtual().getMontoTotal() - totalReserva));
        alojamiento.setActivo(false);

        return nuevaReserva;
    }

    //no se hace uso
    @Override
    public ArrayList<Reserva> listarReservas() throws Exception {
        if (reservas == null || reservas.isEmpty()) {
            throw new Exception("No se encontraron reservas.");
        }
        return new ArrayList<>(reservas);
    }

    //no se hace uso, se debe de utilizar en el controlador de alojamientos más rentables
    @Override
    public ArrayList<Reserva> listaMasRentables(int limite) throws Exception {
        ArrayList<Reserva> reservasRentables = new ArrayList<>();

        for (Reserva reserva : reservas) {
            if (reserva.isEstadoReserva() && reserva.isPagado()) {
                reservasRentables.add(reserva);
            }
        }
        reservasRentables.sort((r1, r2) -> Float.compare(r2.getFactura().getTotal(), r1.getFactura().getTotal()));

        if (reservasRentables.size() > limite) {
            return new ArrayList<>(reservasRentables.subList(0, limite));
        }

        return reservasRentables;
    }

    //no se hace uso, perfil controlador
    @Override
    public List<Reserva> listarReservasCliente(String cedulaCliente) throws Exception {
        if (cedulaCliente == null || cedulaCliente.isEmpty()) {
            throw new Exception("Debe ingresar una cédula válida");
        }
        List<Reserva> reservasPersona = new ArrayList<>();
        for (Reserva reserva : reservas) {
            if (reserva.getCliente().getCedula().equals(cedulaCliente)) {
                reservasPersona.add(reserva);
            }
        }
        if (reservasPersona.isEmpty()) {
            throw new Exception("No se encontraron reservas para la persona con cédula: " + cedulaCliente);
        }
        return reservasPersona;
    }

    //si se hace uso en perfil controlador
    @Override
    public boolean cancelarReserva(Reserva reserva) throws Exception {
        if (reserva == null) {
            throw new Exception("La reserva no puede ser nula.");
        }

        if (!reserva.isEstadoReserva()) {
            throw new Exception("La reserva ya está cancelada.");
        }
        reserva.setEstadoReserva(false);

        Alojamiento alojamiento = reserva.getAlojamiento();
        if (alojamiento != null) {
            alojamiento.setActivo(true);
        } else {
            throw new Exception("No se encontró un alojamiento asociado a la reserva.");
        }

        // Opcional: reembolsar el monto de la reserva al cliente
        Cliente cliente = reserva.getCliente();
        if (cliente != null && reserva.getFactura() != null) {
            float total = reserva.getFactura().getTotal();
            BilleteraVirtual billetera = cliente.getBilleteraVirtual();
            if (billetera != null) {
                billetera.setMontoTotal(billetera.getMontoTotal() + total);
            } else {
                throw new Exception("El cliente no tiene una billetera virtual asociada.");
            }
        }

        return true;
    }

    //se hace uso en el controlador de reserva alojamiento
    @Override
    public Factura generarFactura(Reserva reserva) throws Exception {
        if (reserva == null) {
            throw new Exception("La reserva no puede ser nula.");
        }
        Alojamiento alojamiento = reserva.getAlojamiento();
        int numHuespedes = reserva.getNumHuespedes();
        float subtotal = alojamiento.getValorNoche() * (float) reserva.getFechaInicio().until(reserva.getFechaFin(), java.time.temporal.ChronoUnit.DAYS);

        // Aquí podrías aplicar descuentos, si los hubiera
        float descuento = 0;
        if (alojamiento.isOfertaEspecial()) {
            descuento = subtotal * 0.10f; // Suponemos que si tiene oferta, aplica un 10% de descuento
        }
        float total = subtotal - descuento;
        String codigoFactura = java.util.UUID.randomUUID().toString();

        Factura factura = Factura.builder()
                .codigo(codigoFactura)
                .subtotal(subtotal)
                .total(total)
                .reserva(reserva)
                .fecha(java.time.LocalDateTime.now())
                .cliente(reserva.getCliente())
                .alojamiento(alojamiento)
                .numHuespedes(numHuespedes)
                .descuento(descuento)
                .build();

        return factura;
    }

    //se hace uso en reserva controlador
    @Override
    public float calcularCostoReserva(Reserva reserva) throws Exception {
        if (reserva == null) {
            throw new Exception("La reserva no puede ser nula.");
        }

        Alojamiento alojamiento = reserva.getAlojamiento();
        if (alojamiento == null) {
            throw new Exception("No se encontró un alojamiento asociado a la reserva.");
        }

        // Calcular el número de noches entre las fechas de inicio y fin
        long noches = java.time.temporal.ChronoUnit.DAYS.between(reserva.getFechaInicio(), reserva.getFechaFin());
        if (noches <= 0) {
            throw new Exception("La reserva debe ser para al menos una noche.");
        }

        float costoTotal = noches * alojamiento.getValorNoche();

        // Añadir costo adicional si es una casa o apartamento (si aplica)
        if (alojamiento.getTipoAlojamiento() == TipoAlojamiento.CASA || alojamiento.getTipoAlojamiento() == TipoAlojamiento.APARTAMENTO) {
            costoTotal += alojamiento.getCostoAseoMantenimiento();
        }

        return costoTotal;
    }

    //hace uso de otro método
    @Override
    public float aplicarDescuentos(Alojamiento alojamiento, float porcentaje) throws Exception {
        try {
            if (!alojamientos.contains(alojamiento)) {
                throw new Exception("Alojamiento no encontrado.");
            }

            if (porcentaje < 0 || porcentaje > 100) {
                throw new Exception("El porcentaje de descuento debe estar entre 0 y 100.");
            }

            float descuento = (alojamiento.getValorNoche() * porcentaje) / 100;
            float nuevoValor = alojamiento.getValorNoche() - descuento;

            return nuevoValor;
        } catch (Exception e) {
            throw new Exception("Error al aplicar descuento: " + e.getMessage());
        }
    }

    //no se hace uso
    @Override
    public float crearTarifaDescuento(Alojamiento alojamiento, LocalDate fechaInicio, LocalDate fechaFin, float descuento) throws Exception {
        try {
            if (!alojamientos.contains(alojamiento)) {
                throw new Exception("Alojamiento no encontrado.");
            }

            if (descuento < 0 || descuento > 100) {
                throw new Exception("El descuento debe estar entre 0 y 100.");
            }

            LocalDate fechaActual = LocalDate.now();
            if (fechaActual.isBefore(fechaInicio) || fechaActual.isAfter(fechaFin)) {
                throw new Exception("El descuento no es válido en esta fecha.");
            }
            return aplicarDescuentos(alojamiento, descuento);

        } catch (Exception e) {
            throw new Exception("Error al crear tarifa con descuento: " + e.getMessage());
        }
    }

    //si se hace uso, controlador de crear oferta
    @Override
    public void crearOfertaEspecial(Alojamiento alojamiento, LocalDate fechaInicio, LocalDate fechaFin, float descuento) throws Exception {
        if (alojamiento == null) {
            throw new Exception("El alojamiento no puede ser nulo.");
        }

        if (fechaInicio == null || fechaFin == null) {
            throw new Exception("Las fechas de inicio y fin no pueden ser nulas.");
        }

        if (descuento < 0 || descuento > 1) {  // El descuento debe ser entre 0 y 1 (por ejemplo, 0.10 para un 10% de descuento)
            throw new Exception("El descuento debe estar entre 0 y 1.");
        }

        if (fechaInicio.isAfter(fechaFin)) {
            throw new Exception("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }

        alojamiento.setOfertaEspecial(true);
        alojamiento.setFechaInicioOferta(fechaInicio);
        alojamiento.setFechaFinOferta(fechaFin);
        alojamiento.setDescuento(descuento);

    }

    //si se hace uso
    @Override
    public void editarOferta(Alojamiento alojamiento, LocalDate nuevaFechaInicio, LocalDate nuevaFechaFin, float nuevoDescuento) throws Exception {
        // Verificar si el alojamiento tiene una oferta especial
        if (alojamiento.isOfertaEspecial()) {
            // Actualizar las fechas de la oferta y el descuento
            alojamiento.setFechaInicioOferta(nuevaFechaInicio);
            alojamiento.setFechaFinOferta(nuevaFechaFin);
            alojamiento.setDescuento(nuevoDescuento);

            // Verificar si la nueva oferta sigue siendo válida (las fechas no pueden ser antes de la fecha actual)
            if (nuevaFechaInicio.isBefore(LocalDate.now()) || nuevaFechaFin.isBefore(LocalDate.now())) {
                alojamiento.setOfertaEspecial(false); // Si la fecha no es válida, la oferta deja de ser especial
            }

            // Actualizar la lista de alojamientos con ofertas especiales
            ObservableList<Alojamiento> ofertasEspeciales = listarOfertasEspeciales();
            // Aquí puedes añadir lógica para actualizar cualquier otra lista o vista asociada a la UI

        } else {
            throw new Exception("El alojamiento no tiene una oferta especial para editar.");
        }
    }


    //si se hace uso
    @Override
    public boolean eliminarOferta(String nombreAlojamiento) throws Exception {
        try {
            ArrayList<Alojamiento> alojamientosPorNombre = listarAlojamientos(nombreAlojamiento);
            if (alojamientosPorNombre != null && !alojamientosPorNombre.isEmpty()) {
                for (Alojamiento alojamiento : alojamientosPorNombre) {
                    if (alojamiento.getFechaInicioOferta() != null && alojamiento.getFechaFinOferta() != null) {
                        alojamiento.setFechaInicioOferta(null);
                        alojamiento.setFechaFinOferta(null);
                        alojamiento.setDescuento(0);
                        alojamiento.setOfertaEspecial(false);

                        listarOfertasEspeciales().remove(alojamiento);
                    }
                }
                return true;
            } else {
                throw new Exception("No se encontraron alojamientos con el nombre especificado.");
            }
        } catch (Exception e) {
            throw new Exception("Error al eliminar la oferta: " + e.getMessage());
        }
    }

    //se hace uso en el controlador de billetera virtual
    @Override
    public void recargarBilleteraVirtual(Cliente cliente, float monto) throws Exception {
        if (cliente == null) {
            throw new Exception("El cliente no puede ser nulo.");
        }
        if (monto <= 0) {
            throw new Exception("El monto de recarga debe ser positivo.");
        }

        BilleteraVirtual billetera = cliente.getBilleteraVirtual();
        if (billetera == null) {
            throw new Exception("El cliente no tiene una billetera virtual asociada.");
        }
        billetera.setMontoTotal(billetera.getMontoTotal() + monto);
    }

    //no se hace uso
    @Override
    public String agregarResena(Reserva reserva, String comentario, int calificacion) throws Exception {
        if (reserva == null) {
            throw new Exception("La reserva no puede ser nula.");
        }

        // Verificar que la reserva esté finalizada para permitir agregar una reseña
        if (!reserva.isEstadoReserva()) {
            throw new Exception("Solo se pueden agregar reseñas a reservas finalizadas.");
        }

        if (comentario == null || comentario.trim().isEmpty()) {
            throw new Exception("El comentario de la reseña no puede estar vacío.");
        }

        if (calificacion < 1 || calificacion > 5) {
            throw new Exception("La calificación debe estar entre 1 y 5.");
        }

        Resena nuevaResena = new Resena(reserva.getCliente(), comentario, calificacion, LocalDate.now());
        reserva.getAlojamiento().getResenas().add(nuevaResena);

        return "Reseña agregada exitosamente.";
    }

    //cambiar metodo
    public int verEstadisticas(String ciudad) throws Exception {
        try {
            if (ciudad == null || ciudad.trim().isEmpty()) {
                throw new Exception("El nombre de la ciudad no puede ser nulo o vacío.");
            }

            long cantidadAlojamientos = alojamientos.stream()
                    .filter(alojamiento -> alojamiento.getTipoCiudad().toString().equalsIgnoreCase(ciudad)
                            && alojamiento.isActivo())
                    .count();

            if (cantidadAlojamientos == 0) {
                throw new Exception("No se encontraron alojamientos disponibles en la ciudad especificada.");
            }

            return (int) cantidadAlojamientos;

        } catch (Exception e) {
            throw new Exception("Error al obtener las estadísticas: " + e.getMessage(), e);
        }
    }

    //hace uso de otro método
    @Override
    public CreacionAlojamiento crearAlojamiento(TipoAlojamiento tipoAlojamiento){
        CreacionAlojamiento creacionAlojamiento;
        if(tipoAlojamiento == TipoAlojamiento.APARTAMENTO){
            creacionAlojamiento = new AlojamientoApartamento();
        } else if (tipoAlojamiento == TipoAlojamiento.CASA){
            creacionAlojamiento = new AlojamientoCasa();
        } else if (tipoAlojamiento == TipoAlojamiento.HOTEL){
            creacionAlojamiento = new AlojamientoHotel();
        } else {
            throw new IllegalArgumentException("Tipo de alojamiento desconocido: " + tipoAlojamiento);
        }
        return creacionAlojamiento;  // Devolver el objeto creado
    }


    //hace uso del controlador crear alojamiento
    @Override
    public Alojamiento crearAlojamiento(String nombre, String descripcion, String imagen, float valorNoche, int numHuespedes, List<String> serviciosIncluidos, TipoAlojamiento tipoAlojamiento, TipoCiudad tipoCiudad, boolean activo) throws Exception {
        try {
            if (nombre.isEmpty() || descripcion.isEmpty() || imagen == null
                    || valorNoche < 0 || numHuespedes < 0 || serviciosIncluidos.isEmpty()
                    || tipoAlojamiento == null || tipoCiudad == null) {
                throw new Exception("Todos los campos son obligatorios");
            }

            // Creando la instancia correcta de Alojamiento según el tipo
            CreacionAlojamiento creacionAlojamiento = crearAlojamiento(tipoAlojamiento);
            if (creacionAlojamiento == null) {
                throw new Exception("Tipo de alojamiento no reconocido");
            }

            // Creando el objeto Alojamiento a partir de la fábrica
            Alojamiento alojamiento = creacionAlojamiento.crearOrdenAlojamiento(
                    nombre, descripcion, imagen, valorNoche, numHuespedes,
                    serviciosIncluidos, tipoAlojamiento, tipoCiudad);
            System.out.println("Alojamiento creado: " + alojamiento);
            alojamiento.setActivo(activo); // Asignar el estado activo
            alojamientos.add(alojamiento); // Añadir a la lista de alojamientos
            System.out.println("Tamaño de alojamientos: " + alojamientos.size());

            return alojamiento;
        } catch (Exception e) {
            throw new Exception("No se pudo crear el alojamiento: " + e.getMessage());
        }
    }

    //no se hace uso
    @Override
    public Administrador cambiarPassword(String email, String nuevaPassword) throws Exception {
        String usuarioAdministrador = "admin@bookyourstay.com";

        if (!email.equals(usuarioAdministrador)) {
            throw new Exception("Administrador no encontrado con el email proporcionado.");
        }

        if (nuevaPassword == null || nuevaPassword.length() < 6) {
            throw new Exception("La nueva contraseña debe tener al menos 6 caracteres.");
        }

        Administrador admin = new Administrador(email, nuevaPassword);
        admin.setEmail(email);
        admin.setPassword(nuevaPassword);

        // guardarDatosAdmin();

        return admin;
    }

    @Override
    public boolean verificarCodigoActivacion(String cedula, String codigoActivacion) throws Exception {
        // Validar parámetros de entrada
        if (cedula == null || cedula.trim().isEmpty()) {
            throw new Exception("La cédula no puede ser nula o vacía.");
        }
        if (codigoActivacion == null || codigoActivacion.trim().isEmpty()) {
            throw new Exception("El código de activación no puede ser nulo o vacío.");
        }

        // Obtener el cliente
        Cliente cliente = obtenerCliente(cedula);
        if (cliente == null) {
            throw new Exception("Cliente no encontrado con la cédula proporcionada.");
        }

        // Validar el código de activación
        if (!codigoActivacion.equals(cliente.getCodigoActivacion())) {
            throw new Exception("El código de activación es incorrecto.");
        }

        // Código válido
        return true;
    }

    @Override
    public Cliente cambiarPasswordC(String cedula, String nuevaPassword, String codigoActivacion) throws Exception {
        // Verificar el código de activación
        if (!verificarCodigoActivacion(cedula, codigoActivacion)) {
            throw new Exception("Código de activación inválido.");
        }
        Cliente cliente = obtenerCliente(cedula);
        if (cliente == null) {
            throw new Exception("Cliente no encontrado con la cédula proporcionada.");
        }
        if (nuevaPassword == null || nuevaPassword.length() < 6) {
            throw new Exception("La nueva contraseña debe tener al menos 6 caracteres.");
        }
        cliente.setPassword(nuevaPassword);

        // guardarDatosEmpresa();

        return cliente;
    }

    // Método para generar un código QR a partir del código de la factura
    @Override
    public String generarCodigoQR(Factura factura) throws Exception {
        if (factura == null || factura.getCodigo() == null) {
            throw new Exception("La factura no puede ser nula o tener un código nulo.");
        }

        String codigoFactura = factura.getCodigo(); // Código único de la factura
        String filePath = "codigoQR_" + codigoFactura + ".png"; // Ruta para guardar el archivo
        int width = 300; // Ancho del QR
        int height = 300; // Alto del QR

        // Configuración para la codificación del QR
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        try {
            // Crear el QR
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(
                    codigoFactura,
                    BarcodeFormat.QR_CODE,
                    width,
                    height,
                    hints
            );

            // Guardar el QR en el sistema de archivos
            Path path = FileSystems.getDefault().getPath(filePath);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

            return filePath; // Devuelve la ruta del archivo generado

        } catch (WriterException | IOException e) {
            throw new Exception("Error al generar el código QR: " + e.getMessage(), e);
        }
    }

    @Override
    public String enviarFacturaQR(Reserva reserva) throws Exception {
        // Primero, generamos la factura para la reserva
        Factura factura = generarFactura(reserva);

        // Generar el código QR de la factura
        String codigoQRFilePath = generarCodigoQR(factura); // Usamos tu método para generar el QR

        // Crear el mensaje del correo
        String mensaje = "Hola " + reserva.getCliente().getNombre() + ",\n\n"
                + "Gracias por realizar tu reserva con nosotros. A continuación, encontrarás los detalles de tu reserva:\n\n"
                + "Código de Factura: " + factura.getCodigo() + "\n"
                + "Subtotal: " + factura.getSubtotal() + "\n"
                + "Descuento: " + factura.getDescuento() + "\n"
                + "Total: " + factura.getTotal() + "\n\n"
                + "Detalles de la reserva:\n"
                + "Alojamiento: " + reserva.getAlojamiento().getNombre() + "\n"
                + "Fecha de inicio: " + reserva.getFechaInicio() + "\n"
                + "Fecha de fin: " + reserva.getFechaFin() + "\n"
                + "Número de huéspedes: " + reserva.getNumHuespedes() + "\n\n"
                + "Adjuntamos el código QR de tu factura para tu referencia.\n\n"
                + "¡Esperamos que disfrutes tu estancia con nosotros!\n\n"
                + "Saludos cordiales,\n"
                + "El equipo de BookYourStay";

        // Crear un objeto de EnvioEmail, pasando el archivo QR como adjunto
        EnvioEmail envioEmail = new EnvioEmail(
                reserva.getCliente().getEmail(),
                "Confirmación de Reserva y Factura",
                codigoQRFilePath);  // Pasamos la ruta del archivo QR

        // Enviar el correo
        envioEmail.enviarNotificacion();
        return codigoQRFilePath;
    }

    @Override
    public boolean validarCodigoVerificacion(String codigoIngresado) throws Exception {
        if (codigoIngresado == null || codigoIngresado.isEmpty()) {
            throw new Exception("El código ingresado no puede estar vacío.");
        }

        if (!codigoIngresado.equals(generarCodigoVerificacion())) {
            throw new Exception("El código de verificación es incorrecto.");
        }

        return true;
    }
}
