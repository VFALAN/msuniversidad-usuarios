package com.vf.dev.msuniversidadusuarios.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "PLANTELES")
public class PlantelEntity extends GenericTable {
    @Id
    @Column(name = "ID_PLANTEL")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPlantel;
    @Column(name = "CLAVE")
    private String clave;
    @Column(name = "NOMBRE")
    private String nombre;

    @JoinColumn(name = "ID_MUNICIPIO")
    @ManyToOne
    private MunicipioEntity idMunicipio;

    @OneToMany(mappedBy = "plantel")
    private List<PlantelCarreraEntity> plantelCarreraList;

    @OneToMany(mappedBy = "idPlantel")
    private List<UsuarioEntity> usuarios;

}
