package co.edu.uniquindio.bookyourstay.modelo;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Factura {

    private String codigo;            // Código único de la factura
    private float subtotal;           // Subtotal (precio base sin impuestos o descuentos)
    private float total;              // Total (con impuestos y descuentos si aplica)
    private Reserva reserva;          // Reserva asociada a la factura
    private LocalDateTime fecha;      // Fecha de creación de la factura
    private Cliente cliente;          // Cliente que realizó la reserva
    private Alojamiento alojamiento;  // Alojamiento reservado
    private int numHuespedes;         // Número de huéspedes por los que se genera la factura
    private float descuento;          // Descuento aplicado (si hay)

}
