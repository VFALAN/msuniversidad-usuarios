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

@Entity
@Table(name = "ESTADOS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstadoEntity extends GenericTable implements Serializable {

    @Id
    @Column(name = "ID_ESTADO")

    @JsonProperty
    private Integer idEstado;
    @JsonProperty
    @Column(name = "CODIGO_ESTADO")

    private String codigoEstado;
    @JsonProperty
    @Column(name = "NOMBRE")

    private String nombre;
    @JsonProperty
    @Column(name = "NOMBRE_ABREBIADO")

    private String nombreAbrebiado;
    @JsonIgnore
    @OneToMany(mappedBy = "idEstado")
    private List<MunicipioEntity> municipios;
}
