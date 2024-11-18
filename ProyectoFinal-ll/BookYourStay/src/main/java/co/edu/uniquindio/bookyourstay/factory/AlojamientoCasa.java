package co.edu.uniquindio.bookyourstay.factory;

import co.edu.uniquindio.bookyourstay.modelo.Alojamiento;
import co.edu.uniquindio.bookyourstay.modelo.Resena;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoAlojamiento;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoCiudad;
import co.edu.uniquindio.bookyourstay.servicio.CreacionAlojamiento;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class AlojamientoCasa implements CreacionAlojamiento {
    @Override
    public Alojamiento crearOrdenAlojamiento(String nombre, String descripcion, String imagen, float valorNoche, int numHuespedes, List<String> serviciosIncluidos, TipoAlojamiento tipoAlojamiento, TipoCiudad tipoCiudad) throws Exception {
        return Alojamiento.builder()
                .codigo(UUID.randomUUID().toString())
                .nombre(nombre)
                .descripcion(descripcion)
                .imagen(imagen)
                .valorNoche(valorNoche)
                .capacidadMaxima(numHuespedes)
                .serviciosIncluidos(serviciosIncluidos)
                .tipoAlojamiento(TipoAlojamiento.CASA)
                .tipoCiudad(tipoCiudad)
                .costoAseoMantenimiento(10000)
                .activo(true)
                .ofertaEspecial(false)
                .build();
    }
}
