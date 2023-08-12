package com.vf.dev.msuniversidadusuarios.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Table
@Entity(name = "ESTATUS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstatusEntity extends GenericTable implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ESTATUS")
    @JsonProperty
    private Integer idEstatus;
    @Column(name = "CLAVE")
    @JsonProperty
    private String clave;
    @Column(name = "NOMBRE")
    @JsonProperty
    private String nombre;

    @OneToMany(mappedBy = "idEstatus")
    @JsonIgnore
    private List<UsuarioEntity> usuarios;
}