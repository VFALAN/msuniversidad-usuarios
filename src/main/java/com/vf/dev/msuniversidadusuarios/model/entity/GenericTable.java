package com.vf.dev.msuniversidadusuarios.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
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
    private boolean indActivo;
}
