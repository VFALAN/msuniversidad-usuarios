package com.vf.dev.msuniversidadusuarios.repository;

import com.vf.dev.msuniversidadusuarios.model.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUsuarioRepository extends JpaRepository<UsuarioEntity , Integer> {
    @Query("SELECT u FROM UsuarioEntity u WHERE  u.correo = :correo")
    public List<UsuarioEntity> findByCorreo(@Param("correo")String pCorreo);

    @Query("SELECT u FROM UsuarioEntity u WHERE u.nombreUsuario =:nombreUsuario")
    public List<UsuarioEntity> findByNombreUsuario(@Param("nombreUsuario")String pNombreUsuario);
}
