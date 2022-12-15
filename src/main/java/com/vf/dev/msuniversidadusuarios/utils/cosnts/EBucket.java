package com.vf.dev.msuniversidadusuarios.utils.cosnts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum EBucket {
    BUCKET_REPORTES("reportes",1),
    BUCKET_FORMATOS("formatos",2),
    BUCKETS_INFORMACION_PERSONAL("informacionpersonal",3),
    BUCKET_TAREAS("tareas",4);


    private String name;
    private Integer id;
}
