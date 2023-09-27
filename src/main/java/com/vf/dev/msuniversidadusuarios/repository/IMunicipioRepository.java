package com.vf.dev.msuniversidadusuarios.repository;

import com.vf.dev.msuniversidadusuarios.model.entity.EstadoEntity;
import com.vf.dev.msuniversidadusuarios.model.entity.MunicipioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMunicipioRepository extends JpaRepository<MunicipioEntity, Integer> {

    @Query("SELECT m FROM MunicipioEntity m WHERE m.indActivo = true AND m.idEstado = :estado ORDER BY m.nombre ASC")
    List<MunicipioEntity> findllByEstado(@Param("estado") EstadoEntity pEstadoEntity);

}
