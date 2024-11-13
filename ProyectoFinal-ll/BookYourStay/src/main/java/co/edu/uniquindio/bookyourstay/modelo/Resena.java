package co.edu.uniquindio.bookyourstay.modelo;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
@ToString
public class Resena {

    private Cliente cliente;
    private String comentario;
    private int calificacion;
    private LocalDate fechaCreacion;

}
