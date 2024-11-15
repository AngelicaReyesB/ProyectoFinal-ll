package co.edu.uniquindio.bookyourstay.modelo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Sesion implements Serializable {

    private Cliente cliente;
    private Alojamiento reservarAlojamiento;
    private Alojamiento alojamientoAleatorio;
    private Alojamiento alojamientoDetalle;
    private ArrayList<Cliente> clientes;
    private List<Alojamiento> alojamientos;
    private boolean administrador;
    private static Sesion INSTANCIA;

    public static Sesion getInstancia() {
        if (INSTANCIA == null) {
            INSTANCIA = new Sesion();
        }
        return INSTANCIA;
    }

    public Sesion (){
        cliente = null;
        reservarAlojamiento = null;
        alojamientoAleatorio = null;
        alojamientoDetalle = null;
        clientes = null;
        alojamientos = null;
        administrador = false;
    }

    public void cerrarSesion(){
        cliente = null;
        reservarAlojamiento = null;
        alojamientoAleatorio = null;
        alojamientoDetalle = null;
        clientes = null;
        alojamientos = null;
        administrador = false;
    }
}