package com.vf.dev.msuniversidadusuarios.model.dto.usuario;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UsuarioDTO implements Serializable {
    @JsonProperty
    private Integer idUsuario;
    @JsonProperty
    private String apellidoPaterno;
    @JsonProperty
    private String apellidoMaterno;
    @JsonProperty
    private String nombre;
    @JsonProperty
    private String correo;
    @JsonProperty
    private String folio;
    @JsonProperty
    private String matricula;
    @JsonProperty
    private String nombreUsuario;
    @JsonProperty
    private String password;
    @JsonProperty
    private Integer idEstatus;
    @JsonProperty
    private Integer idPerfil;
    @JsonProperty
    private String curp;
    @JsonProperty
    private String desGenero;
    @JsonProperty
    private Integer edad;
    @JsonProperty
    private Date fechaNacimiento;
    @JsonProperty
    private Integer idCarrera;
    @JsonProperty
    private Integer idPlantel;
    @JsonProperty
    private Integer genero;
    // informacion de la direccion
    @JsonProperty
    private Integer idAsentamiento;
    @JsonProperty
    private String asentamiento;
    @JsonProperty
    private String desFachada;
    @JsonProperty
    private String calle;
    @JsonProperty
    private String numeroExterior;
    @JsonProperty
    private String numeroInterior;
    @JsonProperty
    private Integer idDireccion;
    @JsonProperty
    private Integer idMunicipio;
    @JsonProperty
    private String municipio;
    @JsonProperty
    private Integer idEstado;
    @JsonProperty
    private String estado;
    @JsonProperty
    private String colonia;

}
