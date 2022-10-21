package com.vf.dev.msuniversidadusuarios.repository;

import com.vf.dev.msuniversidadusuarios.model.entity.DireccionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDireccionRespoitory extends JpaRepository<DireccionEntity, Integer> {
}
