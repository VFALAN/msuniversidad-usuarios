package com.vf.dev.msuniversidadusuarios.services.perfil;

import com.vf.dev.msuniversidadusuarios.model.entity.PerfilEntity;
import com.vf.dev.msuniversidadusuarios.utils.exception.MsUniversidadException;

public interface IPerfilService {

    PerfilEntity findById(Integer pIdPerfil) throws MsUniversidadException;
}
