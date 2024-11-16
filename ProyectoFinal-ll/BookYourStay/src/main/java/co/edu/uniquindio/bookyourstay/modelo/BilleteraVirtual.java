package co.edu.uniquindio.bookyourstay.modelo;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class BilleteraVirtual {

    private float montoTotal;

    public BilleteraVirtual() {
        this.montoTotal = 0.0f;
    }
}
