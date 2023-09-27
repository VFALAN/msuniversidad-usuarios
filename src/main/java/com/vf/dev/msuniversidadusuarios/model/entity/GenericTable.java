package com.vf.dev.msuniversidadusuarios.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
public abstract class GenericTable implements Serializable {
    @JsonProperty
    @Column(name="FECHA_ACTUALIZACION")
    private Date fechaActualizacion;
    @JsonProperty
    @Column(name = "FECHA_BAJA")
    private Date fechaBaja;
    @JsonProperty
    @Column(name = "FECHA_ALTA")
    private Date fechaAlta;
    @JsonProperty
    @Column(name = "IND_ACTIVO")
    private Boolean indActivo;
}
