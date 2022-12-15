package com.vf.dev.msuniversidadusuarios.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "CARRERAS")
public class CarreraEntity extends GenericTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CARRERA")
    private Integer idCarrera;
    @Column(name = "NOMBRE")
    private String nombre;

    @OneToMany(mappedBy = "carrera")
    private List<UsuarioEntity> usuarios;

    @OneToMany(mappedBy = "carrera")
    private List<PlantelCarreraEntity> plantelCarreraEntityList;
}
