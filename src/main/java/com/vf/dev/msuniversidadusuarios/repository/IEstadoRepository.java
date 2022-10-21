package com.vf.dev.msuniversidadusuarios.repository;

import com.vf.dev.msuniversidadusuarios.model.entity.EstadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEstadoRepository extends JpaRepository<EstadoEntity , Integer> {
}
