package com.vf.dev.msuniversidadusuarios.model.dto.usuario;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UsuarioDTO implements Serializable{
    @JsonProperty
    private Integer idUsuario;
    @JsonProperty
    @NotBlank(message = "El apellido paterno es requerido")
    private String apellidoPaterno;
    @JsonProperty
    @NotBlank(message = "El apellido materno es requerido")
    private String apellidoMaterno;
    @JsonProperty
    @NotBlank(message = "El nombre es requerido")
    private String nombre;
    @JsonProperty
    @NotBlank(message = "El correo es requerido")
    private String correo;
    @JsonProperty
    private String folio;
    @JsonProperty
    private String matricula;
    @JsonProperty
    @NotBlank(message = "El nombre de Usuario es requerido")
    private String nombreUsuario;
    @JsonProperty
    @NotBlank(message = "La Contraseña es requerida")
    private String password;
    @JsonProperty
    @NotBlank(message = "El estatus es requerido")
    private Integer idEstatus;
    @JsonProperty
    @NotBlank(message = "El perfil es requerido")
    private Integer idPerfil;
    @JsonProperty
    @NotBlank(message = "Curp es requerido")
    private String curp;
    @JsonProperty
    private String desGenero;
    @JsonProperty
    @NotBlank(message = "Edad es requerida")
    private Integer edad;
    @JsonProperty
    @NotBlank(message = "Fecha de nacimiento")
    private Date fechaNacimiento;
    @JsonProperty
    private Integer idCarrera;
    @JsonProperty
    private Integer idPlantel;
    @JsonProperty
    @NotBlank(message = "El Genero es requerido")
    private Integer genero;
    // informacion de la direccion
    @JsonProperty
    @NotBlank(message = "El Asentamiento es requerido")
    private Integer idAsentamiento;
    @JsonProperty
    private String asentamiento;
    @JsonProperty
    @NotBlank(message = "La fachada Es requerida es requerido")
    private String desFachada;
    @JsonProperty
    @NotBlank(message = "La calle  Es requerida es requerido")
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
    @NotNull(message = "Fotografia requerida")
    private MultipartFile fotografiaFile;
    @NotNull(message = "Fotografia requerida")
    private MultipartFile curpFile;
    @NotNull(message = "Fotografia requerida")
    private MultipartFile comprobanteFile;
    @NotNull(message = "Fotografia requerida")
    private MultipartFile actaNacimientoFile;

}
