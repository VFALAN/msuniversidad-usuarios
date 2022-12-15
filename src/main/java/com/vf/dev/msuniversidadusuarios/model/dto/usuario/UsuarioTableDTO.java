package com.vf.dev.msuniversidadusuarios.model.dto.usuario;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UsuarioTableDTO implements Serializable {
    @JsonProperty
    private Integer idUsuario;
    @JsonProperty
    private String nombre;
    @JsonProperty
    private String aPaterno;
    @JsonProperty
    private String aMaterno;
    @JsonProperty
    private String idEstatus;
    @JsonProperty
    private String idPerfil;
    @JsonProperty
    private String folio;
    @JsonProperty
    private Boolean indActivo;
    @JsonProperty
    private String estado;
    @JsonProperty
    private String matricula;
    @JsonProperty
    private String municipio;
    @JsonProperty
    private String asentamiento;
    @JsonProperty
    private String codigoPostal;

}
