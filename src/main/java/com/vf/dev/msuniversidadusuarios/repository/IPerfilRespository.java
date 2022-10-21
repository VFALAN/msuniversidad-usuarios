package com.vf.dev.msuniversidadusuarios.repository;

import com.vf.dev.msuniversidadusuarios.model.entity.PerfilEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPerfilRespository extends JpaRepository<PerfilEntity , Integer> {
}
