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

@Table(name = "MUNICIPIOS")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MunicipioEntity extends GenericTable implements Serializable {
    @Id
    @Column(name = "ID_MUNICIPIO")
    @JsonProperty
    private Integer idMunicipio;
    @Column(name = "CODIGO")
    @JsonProperty
    private String codigo;
    @Column(name = "NOMBRE")
    @JsonProperty
    private String nombre;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "ID_ESTADO")
    private EstadoEntity idEstado;
    @JsonIgnore
    @OneToMany(mappedBy = "idMunicipio")
    private List<AsentamientoEntity> asentamientos;
}
