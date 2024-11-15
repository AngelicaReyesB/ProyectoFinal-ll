package co.edu.uniquindio.bookyourstay.factory;

import co.edu.uniquindio.bookyourstay.modelo.Alojamiento;
import co.edu.uniquindio.bookyourstay.modelo.Habitacion;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoAlojamiento;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoCiudad;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoHabitacion;
import co.edu.uniquindio.bookyourstay.servicio.CreacionAlojamiento;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AlojamientoHotel implements CreacionAlojamiento  {
    @Override
    public Alojamiento crearOrdenAlojamiento(String nombre, String descripcion, String imagen, LocalDate fechaEstancia, float valorNoche, int numHuespedes, List<String> serviciosIncluidos, TipoAlojamiento tipoAlojamiento, TipoCiudad tipoCiudad) throws Exception {
        ArrayList<Habitacion> habitaciones = new ArrayList<>();
        Habitacion individual = Habitacion.builder()
                .tipoHabitacion(TipoHabitacion.INDIVIDUAL)
                .build();
        Habitacion dobleEstandar = Habitacion.builder()
                .tipoHabitacion(TipoHabitacion.DOBLE_ESTANDAR)
                .build();
        Habitacion suitePresencial = Habitacion.builder()
                .tipoHabitacion(TipoHabitacion.SUITE_PRESENCIAL)
                .build();
        Habitacion suiteEjecutiva = Habitacion.builder()
                .tipoHabitacion(TipoHabitacion.SUITE_EJECUTIVA)
                .build();
        Habitacion suiteJunior = Habitacion.builder()
                .tipoHabitacion(TipoHabitacion.SUITE_JUNIOR)
                .build();
        Habitacion dobleDeluxe = Habitacion.builder()
                .tipoHabitacion(TipoHabitacion.DOBLE_DELUXE)
                .build();
        habitaciones.add(individual);
        habitaciones.add(dobleEstandar);
        habitaciones.add(suitePresencial);
        habitaciones.add(suiteEjecutiva);
        habitaciones.add(suiteJunior);
        habitaciones.add(dobleDeluxe);

        return Alojamiento.builder()
                .codigo(UUID.randomUUID().toString())
                .nombre(nombre)
                .descripcion(descripcion)
                .imagen(imagen)
                .fechaEstancia(fechaEstancia)
                .valorNoche(valorNoche)
                .serviciosIncluidos(serviciosIncluidos)
                .tipoAlojamiento(TipoAlojamiento.HOTEL)
                .tipoCiudad(tipoCiudad)
                .activo(true)
                .habitaciones(habitaciones)
                .ofertaEspecial(false)
                .build();

        //    private int capacidadMaxima;

        //    private List<Resena> resenas; //no
        //    private boolean ofertaEspecial; //no, es para saber si un alojamiento tiene o no oferta especial
        //
        //    // Nuevos campos para gestionar las ofertas especiales
        //    private LocalDate fechaInicioOferta;
        //    private LocalDate fechaFinOferta;
        //    private float descuento;
    }
}
