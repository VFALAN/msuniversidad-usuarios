package com.vf.dev.msuniversidadusuarios.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "USUARIOS")
public class UsuarioEntity extends GenericTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @JsonProperty
    @Column(name = "ID_USUARIO")
    private Integer idUsuario;
    @JsonProperty
    @Column(name = "APELLIDO_PATERNO")
    private String apellidoPaterno;
    @JsonProperty
    @Column(name = "APELLIDO_MATERNO")
    private String apellidoMaterno;
    @JsonProperty
    @Column(name = "NOMBRE")
    private String nombre;
    @JsonProperty
    @Column(name = "CORREO")
    private String correo;
    @JsonProperty
    @Column(name = "FOLIO")
    private String folio;
    @JsonProperty
    @Column(name = "MATRICULA")
    private String matricula;
    @JsonProperty
    @Column(name = "NOMBRE_USUARIO")
    private String nombreUsuario;
    @JsonProperty
    @Column(name = "PASSWORD")
    private String password;
    @JsonProperty
    @Column(name = "CURP")
    private String curp;
    @JsonProperty
    @Column(name = "DES_GENERO")
    private String desGenero;
    @JsonProperty
    @Column(name = "GENERO")
    private int genero;
    @JsonProperty
    @Column(name = "EDAD")
    private int edad;
    @JsonProperty
    @Column(name = "FECHA_NACIMIENTO")
    private Date fechaNacimiento;
    @JsonIgnore
    @JoinColumn(name = "ID_PERFIL")
    @ManyToOne
    @Basic(optional = false)
    private PerfilEntity idPerfil;
    @JsonIgnore
    @JoinColumn(name = "ID_ESTATUS")
    @ManyToOne
    private EstatusEntity idEstatus;


    @ManyToOne
    @JoinColumn(name = "ID_PLANTEL")
    private PlantelEntity idPlantel;

    @ManyToOne
    @JoinColumn(name = "ID_CARRERA")
    private CarreraEntity idCarrera;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_DIRECCION")
    private DireccionEntity idDireccion;
}
