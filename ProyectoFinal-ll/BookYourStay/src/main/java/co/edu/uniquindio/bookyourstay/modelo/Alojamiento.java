package co.edu.uniquindio.bookyourstay.modelo;

import co.edu.uniquindio.bookyourstay.modelo.enums.TipoAlojamiento;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoCiudad;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@ToString
public class Alojamiento {

    private String codigo; //no
    private String nombre;
    private String descripcion;
    private String imagen;
    private float valorNoche;
    private int capacidadMaxima;
    private List<String> serviciosIncluidos;
    private TipoAlojamiento tipoAlojamiento;
    private TipoCiudad tipoCiudad;
    private boolean activo; //no
    private List<Habitacion> habitaciones; // Solo para hoteles
    private List<Resena> resenas; //no
    private double costoAseoMantenimiento; // Para casa o apartamento
    private boolean ofertaEspecial; //no, es para saber si un alojamiento tiene o no oferta especial

    // Nuevos campos para gestionar las ofertas especiales
    private LocalDate fechaInicioOferta;
    private LocalDate fechaFinOferta;
    private float descuento;
}
