package com.vf.dev.msuniversidadusuarios.utils.cosnts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ETipoArchivo {

    FOTOGRAFIA_REGISTRO(1, "FOTOGRAFIA_REGISTRO"),
    CURP(2, "CURP"),
    ACTA_DE_NACIMINETO(3, "ACTA_DE_NACIMIENTO"),
    COMPROBANTE_DOMICILIO(4, "COMPROBANTE_DOMICILIO"),
    COMPROBANTE_PAGO_INSCRIPCION(5, "COMPROBANTE_PAGO_INSCRIPCION");


    private Integer id;
    private String name;
}
