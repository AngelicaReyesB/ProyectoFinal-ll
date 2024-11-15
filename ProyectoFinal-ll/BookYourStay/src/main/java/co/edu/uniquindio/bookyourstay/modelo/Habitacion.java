package co.edu.uniquindio.bookyourstay.modelo;

import co.edu.uniquindio.bookyourstay.modelo.enums.TipoAlojamiento;
import co.edu.uniquindio.bookyourstay.modelo.enums.TipoHabitacion;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class Habitacion {

    private TipoHabitacion tipoHabitacion;
    private String id;
    private String numero;
    private float precioPorNoche;
    private int capacidad;
    private String imagen;
    private String descripcion;

}
