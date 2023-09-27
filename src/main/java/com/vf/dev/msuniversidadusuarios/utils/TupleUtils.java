package com.vf.dev.msuniversidadusuarios.utils;

import com.vf.dev.msuniversidadusuarios.model.dto.usuario.UsuarioTableDTO;
import com.vf.dev.msuniversidadusuarios.utils.cosnts.EstatusConstans;
import com.vf.dev.msuniversidadusuarios.utils.cosnts.GenericConstans;
import com.vf.dev.msuniversidadusuarios.utils.cosnts.PerfilConstans;
import com.vf.dev.msuniversidadusuarios.utils.cosnts.UsuarioConstants;
import jakarta.persistence.Tuple;

public class TupleUtils {

    public static UsuarioTableDTO buildUsuarioTableFromDTOfromTuple(Tuple t) {
        return UsuarioTableDTO.builder()
                .idUsuario(t.get(UsuarioConstants.ID_USUARIO) != null ? (int) t.get(UsuarioConstants.ID_USUARIO) : null)
                .nombre(t.get(UsuarioConstants.NOMBRE) != null ? (String) t.get(UsuarioConstants.NOMBRE) : null)
                .paterno(t.get(UsuarioConstants.APELLIDO_PATERNO) != null ? (String) t.get(UsuarioConstants.APELLIDO_PATERNO) : null)
                .materno(t.get(UsuarioConstants.APELLIDO_MATERNO) != null ? (String) t.get(UsuarioConstants.APELLIDO_MATERNO) : null)
                .indActivo(t.get(GenericConstans.IND_ACTIVO) != null ? (Boolean) t.get(GenericConstans.IND_ACTIVO) : false)
                .estatus(t.get(EstatusConstans.ID_ESTATUS) != null ? (String) t.get(EstatusConstans.ID_ESTATUS) : null)
                .perfil(t.get(PerfilConstans.ID_PERFIL) != null ? (String) t.get(PerfilConstans.ID_PERFIL) : null)
                .folio(t.get(UsuarioConstants.FOLIO) != null ? (String) t.get(UsuarioConstants.FOLIO) : null)
                .matricula(t.get(UsuarioConstants.MATRICULA) != null ? (String) t.get(UsuarioConstants.MATRICULA) : null)
                .asentamiento(t.get("ASENTAMIENTO") != null ? (String) t.get("ASENTAMIENTO") : null)
                .codigoPostal(t.get("CODIGO_POSTAL") != null ? (String) t.get("CODIGO_POSTAL") : null)
                .municipio(t.get("MUNICIPIO") != null ? (String) t.get("MUNICIPIO") : null)
                .estado(t.get("ESTADO")!=null ? (String) t.get("ESTADO") : null)
                .build();
    }
}
