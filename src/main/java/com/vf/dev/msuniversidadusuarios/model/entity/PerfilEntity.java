package com.vf.dev.msuniversidadusuarios.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
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
    @OneToMany(mappedBy = "perfil")
    private List<UsuarioEntity> usuarios;


}
