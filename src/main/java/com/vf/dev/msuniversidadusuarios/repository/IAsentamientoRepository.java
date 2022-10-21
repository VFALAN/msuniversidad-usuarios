package com.vf.dev.msuniversidadusuarios.repository;

import com.vf.dev.msuniversidadusuarios.model.entity.AsentamientoEntity;
import com.vf.dev.msuniversidadusuarios.model.entity.MunicipioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IAsentamientoRepository extends JpaRepository<AsentamientoEntity, Integer> {


    @Query("SELECT a FROM AsentamientoEntity a WHERE a.indActivo = 1 AND a.idMunicipio = :municipio ORDER BY a.nombre ASC ")
    List<AsentamientoEntity> findAllByMunicipio(@Param("municipio") MunicipioEntity pMunicipio);
}
