package com.vf.dev.msuniversidadusuarios.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "PERFILES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PerfilEntity extends GenericTable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PERFIL")
    @JsonIgnore
    @Basic(optional = false)
    private Integer idPerfil;
    @Column(name = "CLAVE")
    @JsonIgnore
    private String clave;
    @Column(name = "NOMBRE")
    @JsonIgnore
    private String nombre;
    @OneToMany(mappedBy = "idPerfil")
    private List<UsuarioEntity> usuarios;


}
