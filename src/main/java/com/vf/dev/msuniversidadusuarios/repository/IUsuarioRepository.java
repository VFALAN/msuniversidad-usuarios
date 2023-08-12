package com.vf.dev.msuniversidadusuarios.repository;

import com.vf.dev.msuniversidadusuarios.model.dto.usuario.IUsuarioDetalle;
import com.vf.dev.msuniversidadusuarios.model.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {
    @Query("SELECT u FROM UsuarioEntity u WHERE  u.correo = :correo")
    public List<UsuarioEntity> findByCorreo(@Param("correo") String pCorreo);

    @Query("SELECT u FROM UsuarioEntity u WHERE u.nombreUsuario =:nombreUsuario")
    public List<UsuarioEntity> findByNombreUsuario(@Param("nombreUsuario") String pNombreUsuario);

    @Query(value = "SELECT  " +
            "u.ID_USUARIO AS idUsuario, " +
            "u.NOMBRE AS nombre, " +
            "u.CORREO as correo, "+
            "u.NOMBRE_USUARIO as nombreUsuario ,"+
            "u.APELLIDO_PATERNO AS apellidoPaterno, " +
            "u.APELLIDO_MATERNO AS apellidoMaterno, " +
            "DATE_FORMAT(u.FECHA_NACIMIENTO, '%Y/%m/%d') AS fechaNacimiento, " +
            "u.EDAD AS edad, " +
            "u.GENERO AS genero, " +
            "u.DES_GENERO AS desGenero, " +
            "u.CURP AS curp, " +
            "IFNULL (u.MATRICULA, '') AS matricula, " +
            "pe.ID_PERFIL AS idPerfil, " +
            "pe.NOMBRE AS perfil, " +
            "es.ID_ESTATUS AS idEstatus , " +
            "es.NOMBRE AS estatus,  " +
            "d.CALLE AS calle, " +
            "d.DES_FACHADA AS desFachada, " +
            "d.ID_DIRECCION as idDireccion, "+
            "u.IND_ACTIVO as indActivo,"+
            "d.NUMERO_EXTERIOR AS numeroExterior, " +
            "d.NUMERO_INTERIOR AS numeroInterior, " +
            "a.ID_ASENTAMIENTO AS idAsentamiento, " +
            "a.NOMBRE AS asentamiento, " +
            "m.ID_MUNICIPIO AS idMunicipio, " +
            "m.NOMBRE AS municipio, " +
            "e.ID_ESTADO AS idEstado, " +
            "e.NOMBRE AS estado, " +
            "p.ID_PLANTEL AS idPlantel, " +
            "p.NOMBRE AS plantel, " +
            "c.ID_CARRERA AS idCarrera, " +
            "c.NOMBRE AS carrera " +
            "FROM USUARIOS AS u " +
            "INNER JOIN PERFILES AS pe ON u.ID_PERFIL = pe.ID_PERFIL " +
            "INNER JOIN ESTATUS AS es ON u.ID_ESTATUS  = es.ID_ESTATUS " +
            "INNER JOIN DIRECCIONES AS d ON d.ID_DIRECCION = u.ID_DIRECCION " +
            "INNER JOIN ASENTAMIENTOS a ON a.ID_ASENTAMIENTO = d.ID_ASENTAMIENTO " +
            "INNER JOIN MUNICIPIOS m ON m.ID_MUNICIPIO = a.ID_MUNICIPIO " +
            "INNER JOIN ESTADOS e ON e.id_estado = m.id_estado " +

            "LEFT JOIN PLANTELES p ON u.ID_PLANTEL = p.ID_PLANTEL " +
            "LEFT JOIN CARRERAS c ON c.ID_CARRERA = u.ID_CARRERA " +
            "WHERE u.IND_ACTIVO = 1 AND u.ID_USUARIO = :idUsuario", nativeQuery = true)
    IUsuarioDetalle getDetail(@Param("idUsuario") Integer pIdUsuario);

}
