package com.vf.dev.msuniversidadusuarios.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Table(name = "PLANTEL_CARRERA")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class PlantelCarreraEntity extends GenericTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PLANTEL_CARRERA")
    private Integer idPlantelCarrera;
    @ManyToOne
    @JoinColumn(name = "ID_PLANTEL")
    private PlantelEntity plantel;
    @ManyToOne
    @JoinColumn(name = "ID_CARRERA")
    private CarreraEntity carrera;

}
