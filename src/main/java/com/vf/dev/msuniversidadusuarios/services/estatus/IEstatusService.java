package com.vf.dev.msuniversidadusuarios.services.estatus;

import com.vf.dev.msuniversidadusuarios.model.entity.EstatusEntity;
import com.vf.dev.msuniversidadusuarios.utils.exception.MsUniversidadException;

public interface IEstatusService {
    EstatusEntity findById(Integer pEstatusId) throws MsUniversidadException;
}
