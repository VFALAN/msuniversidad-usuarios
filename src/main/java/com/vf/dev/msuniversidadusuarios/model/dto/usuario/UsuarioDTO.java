package com.vf.dev.msuniversidadusuarios.model.dto.usuario;

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
private Integer idUsuario;
private String apellidoPaterno;
private String apellidoMaterno;
private String nombre;
private String correo;
private String folio;
private String matricula;
private String nombreUsuario;
private String password;
private Integer idEstatus;
private Integer idPerfil;
private String curp;
private String desGenero;
private Integer edad;
private Date fechaNacimiento;
private Integer genero;
private Integer idAsentamiento;
private String desFachada;
private String calle;
private String numeroExterior;
private String numeroInterior;



}
