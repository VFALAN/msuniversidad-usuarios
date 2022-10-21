package com.vf.dev.msuniversidadusuarios.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "DIRECCIONES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DireccionEntity extends GenericTable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DIRECION")
    @JsonIgnore
    private Integer idDireccion;
    @Column(name = "CALLE")
    @JsonIgnore
    private String calle;
    @Column(name = "DES_FACHADA")
    @JsonIgnore
    private String desFachada;
    @Column(name = "NUMERO_EXTERIOR")
    @JsonIgnore
    private String numeroExterior;
    @Column(name = "NUMERO_INTERIOR")
    @JsonIgnore
    private String numeroInterior;
    @JsonIgnore
    @JoinColumn(name = "ID_ASENTAMIENTO")
    @ManyToOne
    private AsentamientoEntity idAsentamiento;
    @JsonIgnore
    @OneToOne(mappedBy = "idDireccion")
    private UsuarioEntity usuarioEntity;

}
