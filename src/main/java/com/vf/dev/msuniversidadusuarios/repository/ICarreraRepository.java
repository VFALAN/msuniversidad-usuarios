package com.vf.dev.msuniversidadusuarios.repository;

import com.vf.dev.msuniversidadusuarios.model.entity.CarreraEntity;
import com.vf.dev.msuniversidadusuarios.model.entity.PlantelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICarreraRepository extends JpaRepository<CarreraEntity, Integer> {
@Query("SELECT c FROM CarreraEntity c join c.plantelCarreraEntityList pl where pl.plantel = :idPlantel")
    List<CarreraEntity> findByPlantel(@Param("idPlantel") PlantelEntity pPlantelEntity);
}
