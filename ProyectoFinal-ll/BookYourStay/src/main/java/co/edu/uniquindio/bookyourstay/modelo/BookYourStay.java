package co.edu.uniquindio.bookyourstay.modelo;

import co.edu.uniquindio.bookyourstay.factory.AlojamientoApatamento;
import co.edu.uniquindio.bookyourstay.factory.AlojamientoCasa;
import co.edu.uniquindio.bookyourstay.factory.AlojamientoHotel;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoAlojamiento;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoCiudad;
import co.edu.uniquindio.bookyourstay.servicio.CreacionAlojamiento;
import co.edu.uniquindio.bookyourstay.servicio.ServiciosEmpresa;
import co.edu.uniquindio.bookyourstay.utils.EnvioEmail;
import co.edu.uniquindio.bookyourstay.utils.Persistencia;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class BookYourStay extends Persistencia implements ServiciosEmpresa {
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
            cargarDatosEmpresa();
        }catch ( Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void cargarDatosEmpresa() throws Exception {
        try {
            ArrayList<Cliente> clientesCargados = persistencia.cargarClientes();
            ArrayList<Alojamiento> alojamientosCargados = persistencia.cargarAlojamientos();
            ArrayList<Factura> facturasCargados = persistencia.cargarFacturas();

            if (!clientesCargados.isEmpty()) {
                clientes.addAll(clientesCargados);
            }
            if (!alojamientosCargados.isEmpty()) {
                alojamientos.addAll(alojamientosCargados);
            }
            if (!facturasCargados.isEmpty()) {
                facturas.addAll(facturasCargados);
            }
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void guardarDatosEmpresa() throws Exception {
        try {
            persistencia.guardarAlojamientos(alojamientos);
            persistencia.guardarClientes(clientes);
            persistencia.guardarFacturas(facturas);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Cliente registrarCliente(String cedula, String nombre, String telefono, String email, String password) throws Exception {
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
            guardarDatosEmpresa();


        } catch(Exception e){
            throw new Exception("No se puede agregar el nuevo cliente");
        }
        return cliente;
    }

    @Override
    public Cliente iniciarSesion(String email, String password) throws Exception {
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            throw new Exception("Correo y contraseña no pueden estar vacíos.");
        }

        for (Cliente cliente : clientes) {
            if (cliente.getEmail().equals(email)) {
                if (cliente.getPassword() != null && cliente.getPassword().equals(password)) {
                    return cliente;
                } else {
                    throw new Exception("Correo o contraseña incorrectos.");
                }
            }
        }
        throw new Exception("Correo o contraseña incorrectos.");
    }

    @Override
    public Cliente obtenerCliente(String cedula) throws Exception {
        try {
            for (Cliente cliente : clientes) {
                System.out.println("CLIENTE QUE RECORRO EN BUSCAR" + cliente);
                if (cliente.getCedula().equals(cedula)) {
                    return cliente;
                }
            }
            return null;
        }catch (Exception e){
            throw new Exception("No se puede buscar cliente");
        }
    }

    @Override
    public Cliente validarUsuario(String email, String password) throws Exception {
        Cliente cliente = obtenerCliente(email);
        if(cliente != null){
            if(cliente.getPassword().equals(password)){
                return cliente;
            }else {
                throw new Exception("Datos de ingres son incorrectos");
            }
        } else {
            throw new Exception("El usuario no existe");
        }
    }

    @Override
    public Cliente obtenerUsuario(String email) throws Exception {
        for (Cliente cliente: clientes){
            if(cliente.getEmail().equals(email)){
                return cliente;
            }
        }
        return null;
    }

    @Override
    public Cliente editarCuenta(String cedula, String nombre, String telefono, String email, String password) throws Exception {

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

        guardarDatosEmpresa();
        return cliente;
    }

    @Override
    public boolean eliminarCuentaCliente(String cedulaCliente) throws Exception {
        for (Cliente cliente : clientes) {
            if (cliente.getCedula().equals(cedulaCliente)) {
                return true;
            }
        }
        throw new Exception("No se encontró un cliente con la cédula " + cedulaCliente);
    }

    //FALTAAAA
    @Override
    public Cliente cambiarPasswordC(String cedula, String nuevaPassword) throws Exception {
        return null;
    }

    @Override
    public ArrayList<Cliente> listarClientes() throws Exception {
        return new ArrayList<>(clientes);
    }

    @Override
    public void enviarNotificacion(String destinatario, String asunto, String mensaje) throws Exception {
        try {
            EnvioEmail envioEmail = new EnvioEmail(destinatario, asunto, mensaje);
            envioEmail.enviarNotificacion();
        } catch (Exception e) {
            throw new Exception("No se puede enviar la notificación al correo electrónico", e);
        }
    }

    @Override
    public void enviarCodigoActivacion(Cliente cliente) throws Exception {
        String email = cliente.getEmail();
        String mensajeActivacion = "Recuerde activar la cuenta para poder realizar sus reservas en BookYourStay. Código de activación: " +
                cliente.getCodigoActivacion();
        enviarNotificacion(email, "BookYourStay: Código de activación", mensajeActivacion );
    }

    @Override
    public boolean activarUsuario(String codigoActivacion, Cliente cliente) throws Exception {
        Cliente usuarioEncontrado = validarUsuario(cliente.getCedula(), cliente.getPassword());
        if(usuarioEncontrado != null){
            if(usuarioEncontrado.getCodigoActivacion().equals(codigoActivacion)){
                usuarioEncontrado.setEstadoCuenta(true);
                guardarDatosEmpresa();
                return true;
            }else {
                throw new Exception("El código de activación no coincide");
            }
        }else {
            throw new Exception("El usuario no existe");
        }
    }

    @Override
    public Administrador cambiarPassword(String email, String nuevaPassword) throws Exception {
        return null;
    }

    //la contraseña debe de cambiar?
    @Override
    public boolean validarIngresoAdministrador(String email, String password) throws Exception {
        String usuarioAdministrador = "admin@bookyourstay.com";
        String passwordAdministrador = "admin123";
        return email.equals(usuarioAdministrador) && password.equals(passwordAdministrador);
    }

    @Override
    public Alojamiento crearAlojamiento(String nombre, String descripcion, String imagen, LocalDate fechaEstancia, float valorNoche, int numHuespedes, List<String> serviciosIncluidos, TipoAlojamiento tipoAlojamiento, TipoCiudad tipoCiudad) throws Exception {
        try {
            if (nombre.isEmpty() || descripcion.isEmpty() || imagen == null || fechaEstancia == null || valorNoche <- 0 || numHuespedes<- 0||
                    serviciosIncluidos.isEmpty() || tipoAlojamiento == null || tipoCiudad == null) {
                throw new Exception("Todos los campos son obligatorios");
            }
            CreacionAlojamiento creacionAlojamiento = crearAlojamiento(tipoAlojamiento);
            Alojamiento alojamiento = creacionAlojamiento.crearOrdenAlojamiento(nombre, descripcion, imagen, fechaEstancia, valorNoche, numHuespedes, serviciosIncluidos, tipoAlojamiento, tipoCiudad);
            alojamientos.add(alojamiento);
            guardarDatosEmpresa();
            return alojamiento;
        } catch (Exception e) {
            throw new Exception("No se pudo crear el alojamiento" + e.getMessage());
        }
    }

    @Override
    public Alojamiento obtenerAlojamiento(String codigo) throws Exception {
        for (Alojamiento alojamiento: alojamientos){
            if(alojamiento.getCodigo().equals(codigo)){
                return alojamiento;
            }
        }
        return null;
    }

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

            guardarDatosEmpresa();
        }
        return alojamientoEncontrado;
    }

    @Override
    public ArrayList<Alojamiento> listarAlojamientos() throws Exception {
        ArrayList<Alojamiento> alojamientosActivos = new ArrayList<>();
        for (Alojamiento alojamiento: alojamientos){
            if(alojamiento.isDisponible()){
                alojamientosActivos.add(alojamiento);
            }
        }
        return (ArrayList<Alojamiento>) alojamientos;
    }

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

    @Override
    public ArrayList<Alojamiento> listarAlojamientos(String nombreAlojamiento) throws Exception {
        ArrayList<Alojamiento> alojamientosPorNombre = new ArrayList<>();
        for (Alojamiento alojamiento: alojamientos){
            if(alojamiento.getNombre().contains(nombreAlojamiento)){
                alojamientosPorNombre.add(alojamiento);
                return alojamientosPorNombre;
            }
        }
        return null;
    }

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

    @Override
    public List<Alojamiento> listarOfertasEspeciales() throws Exception {
        List<Alojamiento> alojamientosConOferta = new ArrayList<>();

        for (Alojamiento alojamiento : alojamientos) {
            if (alojamiento.isOfertaEspecial()) {
                alojamientosConOferta.add(alojamiento);
            }
        }

        return alojamientosConOferta;
    }

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

        if (!alojamiento.isDisponible()) {
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

        cliente.getBilleteraVirtual().setMontoTotal(cliente.getBilleteraVirtual().getMontoTotal() - totalReserva);
        alojamiento.setDisponible(false);

        return nuevaReserva;
    }

    @Override
    public ArrayList<Reserva> listarReservas() throws Exception {
        if (reservas == null || reservas.isEmpty()) {
            throw new Exception("No se encontraron reservas.");
        }
        return new ArrayList<>(reservas);
    }

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
            alojamiento.setDisponible(true);
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

    @Override
    public int verEstadisticas(String ciudad) throws Exception {
        return 0;
    }

    //dudas sobre las habitaciones y el costo por mantenimiento, se hace con un if
    @Override
    public CreacionAlojamiento crearAlojamiento(TipoAlojamiento tipoAlojamiento){
        CreacionAlojamiento creacionAlojamiento;
        if(tipoAlojamiento == TipoAlojamiento.APARTAMENTO){
            creacionAlojamiento = new AlojamientoApatamento();
        }else if (tipoAlojamiento == TipoAlojamiento.CASA){
            creacionAlojamiento = new AlojamientoCasa();
        }else if (tipoAlojamiento == TipoAlojamiento.HOTEL){
            creacionAlojamiento = new AlojamientoHotel();
        }
        return null;
    }

    @Override
    public String enviarCodigoQR(Factura factura, String emailCliente) throws Exception {
       return null;
    }


}
