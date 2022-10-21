package com.vf.dev.msuniversidadusuarios.utils.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class MsUniversidadException extends Exception{


    private  String codigoInterno;
    public MsUniversidadException(String pMessage, String pCodigoInterno) {
        super(pMessage);
        this.setCodigoInterno(pCodigoInterno);
    }
}
