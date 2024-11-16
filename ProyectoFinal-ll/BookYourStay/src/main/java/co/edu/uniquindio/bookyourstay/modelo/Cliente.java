package co.edu.uniquindio.bookyourstay.modelo;

import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Cliente {

    private String cedula;
    private String nombre;
    private String telefono;
    private String email;
    private String password;
    private boolean estadoCuenta;
    private String codigoActivacion;
    private BilleteraVirtual billeteraVirtual;
    public Cliente(String cedula, String nombre, String telefono, String email,
                   String password, boolean estadoCuenta, String codigoActivacion) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.password = password;
        this.estadoCuenta = estadoCuenta;
        this.codigoActivacion = codigoActivacion;
        // Inicializa la billetera virtual por defecto
        this.billeteraVirtual = new BilleteraVirtual();
    }
    public static class ClienteBuilder {
        public ClienteBuilder billeteraVirtual(BilleteraVirtual billeteraVirtual) {
            if (billeteraVirtual == null) {
                this.billeteraVirtual = new BilleteraVirtual();
            } else {
                this.billeteraVirtual = billeteraVirtual;
            }
            return this;
        }
    }
}
