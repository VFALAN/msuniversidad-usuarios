package com.vf.dev.msuniversidadusuarios.repository;

import com.vf.dev.msuniversidadusuarios.model.entity.EstadoEntity;
import com.vf.dev.msuniversidadusuarios.model.entity.PlantelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPlantelRepository extends JpaRepository<PlantelEntity, Integer> {

    @Query("SELECT p FROM PlantelEntity p where p.indActivo = true AND p.idMunicipio.idEstado = :estado")
    List<PlantelEntity> listPlanteles(@Param("estado")EstadoEntity estadoEntity);
}
