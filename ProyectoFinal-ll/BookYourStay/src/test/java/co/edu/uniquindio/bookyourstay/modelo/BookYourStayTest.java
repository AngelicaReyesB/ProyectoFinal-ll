package co.edu.uniquindio.bookyourstay.modelo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookYourStayTest {

    @Test
    void testObtenerUsuarioExistente() throws Exception {
        // Crear instancia del servicio
        BookYourStay servicio = new BookYourStay();

        BilleteraVirtual billeteraVirtual = new BilleteraVirtual(50000);

        // Crear un cliente de prueba usando el constructor adecuado
        Cliente cliente = new Cliente(
                "1089481681",   // cedula
                "Camila",       // nombre
                "3104734465",   // telefono
                "camila@example.com", // email
                "12345",        // password
                true,           // estadoCuenta
                "ABC123",       // codigoActivacion
                billeteraVirtual // billeteraVirtual
        );

        // Registrar el cliente
        servicio.registrarCliente(
                cliente.getNombre(),
                cliente.getCedula(),
                cliente.getTelefono(),
                cliente.getEmail(),
                cliente.getPassword()
        );


        // Obtener el usuario registrado por su email
        Cliente clienteObtenido = servicio.obtenerUsuario("camila@example.com");

        // Verificar que el cliente se obtuvo correctamente
        assertNotNull(clienteObtenido);
        assertEquals("camila@example.com", clienteObtenido.getEmail());
        assertEquals("Camila", clienteObtenido.getNombre());
    }

    @Test
    void testObtenerUsuarioInexistente() throws Exception {
        // Crear instancia del servicio
        BookYourStay servicio = new BookYourStay();

        // Intentar obtener un usuario que no existe
        Cliente clienteObtenido = servicio.obtenerUsuario("inexistente@example.com");

        // Verificar que el resultado sea null
        assertNull(clienteObtenido, "El cliente debería ser null si no existe.");
    }

    @Test
    void testObtenerUsuarioEmailNulo() {
        // Crear instancia del servicio
        BookYourStay servicio = new BookYourStay();

        // Intentar obtener un usuario con email null
        Exception exception = assertThrows(NullPointerException.class, () -> {
            servicio.obtenerUsuario(null);
        });

        // Verificar que se lance una excepción
        assertNotNull(exception, "Debería lanzarse una excepción si el email es null.");
    }

    @Test
    void testEditarCuentaExitoso() throws Exception {
        BookYourStay servicio = new BookYourStay();
        Cliente cliente = new Cliente("1089481681", "Camila", "3104734465", "camila@example.com", "12345", true, "ABC123", new BilleteraVirtual(50000));
        servicio.registrarCliente(
                cliente.getNombre(),
                cliente.getCedula(),
                cliente.getTelefono(),
                cliente.getEmail(),
                cliente.getPassword()
        );


        servicio.editarCuenta("1089481681", "Camila Pérez", "3211234567", "camila.perez@example.com", "54321");

        Cliente clienteActualizado = servicio.obtenerCliente("1089481681");
        assertEquals("Camila Pérez", clienteActualizado.getNombre());
        assertEquals("3211234567", clienteActualizado.getTelefono());
        assertEquals("camila.perez@example.com", clienteActualizado.getEmail());
        assertEquals("54321", clienteActualizado.getPassword());
    }

    @Test
    void testEditarCuentaCedulaInvalida() {
        BookYourStay servicio = new BookYourStay();
        Exception exception = assertThrows(Exception.class, () ->
                servicio.editarCuenta(null, "Camila Pérez", "3211234567", "camila.perez@example.com", "54321")
        );
        assertEquals("La cédula es obligatoria para editar la cuenta", exception.getMessage());
    }

    @Test
    void testEliminarCuentaClienteExitoso() throws Exception {
        BookYourStay servicio = new BookYourStay();
        Cliente cliente = new Cliente("1089481681", "Camila", "3104734465", "camila@example.com", "12345", true, "ABC123", new BilleteraVirtual(50000));
        servicio.registrarCliente(
                cliente.getNombre(),
                cliente.getCedula(),
                cliente.getTelefono(),
                cliente.getEmail(),
                cliente.getPassword()
        );


        boolean eliminado = servicio.eliminarCuentaCliente("1089481681");

        assertTrue(eliminado, "El cliente debería haberse eliminado correctamente");
        assertNull(servicio.obtenerCliente("1089481681"), "El cliente no debería existir después de la eliminación");
    }

    @Test
    void testEliminarCuentaClienteInexistente() {
        BookYourStay servicio = new BookYourStay();
        Exception exception = assertThrows(Exception.class, () ->
                servicio.eliminarCuentaCliente("987654321")
        );
        assertEquals("No se encontró un cliente con la cédula 987654321", exception.getMessage());
    }

}
