package co.edu.uniquindio.bookyourstay.modelo;

import co.edu.uniquindio.bookyourstay.modelo.enums.TipoHabitacion;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Reserva {

    private String codigo;
    private Cliente cliente;
    private Alojamiento alojamiento;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int numHuespedes;
    private boolean pagado;
    private  Factura factura;
    private String codigoQR;
    private Resena resena;
    private boolean estadoReserva;
    private LocalDateTime fechaCreacion;

}
