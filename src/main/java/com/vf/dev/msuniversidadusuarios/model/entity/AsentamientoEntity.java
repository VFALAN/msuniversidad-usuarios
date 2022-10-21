package com.vf.dev.msuniversidadusuarios.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "ASENTAMIENTOS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AsentamientoEntity extends GenericTable implements Serializable {
    @Id
    @Column(name = "ID_ASENTAMIENTO")
    @JsonIgnore
    private Integer idAsentamientos;
    @Column(name = "NOMBRE")
    @JsonIgnore
    private String nombre;
    @Column(name = "CODIGO")
    @JsonIgnore
    private String codigo;
    @Column(name = "CODIGO_POSTAL")
    @JsonIgnore
    private String codigoPostal;
    @Column(name = "CODIGO_TIPO_ASENTAMIENTO")
    @JsonIgnore
    private String codigoTipoAsentamiento;
    @Column(name = "TIPO_ASENTAMIENTO")
    @JsonIgnore
    private String tipoAsentamiento;
    @Column(name = "ZONA")
    @JsonIgnore
    private String zona;
    @JoinColumn(name = "ID_MUNICIPIO")
    @ManyToOne
    @JsonIgnore
    private MunicipioEntity idMunicipio;

    @OneToMany(mappedBy = "idAsentamiento")
    @JsonIgnore
    private List<DireccionEntity> direcciones;
}
