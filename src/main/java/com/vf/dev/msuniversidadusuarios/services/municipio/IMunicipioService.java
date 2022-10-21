package com.vf.dev.msuniversidadusuarios.services.municipio;

import com.vf.dev.msuniversidadusuarios.model.dto.ComboDTO;
import com.vf.dev.msuniversidadusuarios.model.entity.EstadoEntity;
import com.vf.dev.msuniversidadusuarios.model.entity.MunicipioEntity;
import com.vf.dev.msuniversidadusuarios.utils.exception.MsUniversidadException;

import java.util.List;

public interface IMunicipioService {

    MunicipioEntity findById(Integer pMuncipioId) throws MsUniversidadException;

    List<ComboDTO> findByestado(EstadoEntity pEstadoEntity);
}
