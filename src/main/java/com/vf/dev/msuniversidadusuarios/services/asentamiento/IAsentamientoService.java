package com.vf.dev.msuniversidadusuarios.services.asentamiento;

import com.vf.dev.msuniversidadusuarios.model.dto.ComboDTO;
import com.vf.dev.msuniversidadusuarios.model.entity.AsentamientoEntity;
import com.vf.dev.msuniversidadusuarios.model.entity.MunicipioEntity;
import com.vf.dev.msuniversidadusuarios.utils.exception.MsUniversidadException;

import java.util.List;

public interface IAsentamientoService {

    AsentamientoEntity findById(Integer pAsentamientoId) throws MsUniversidadException;

    List<ComboDTO> listByMunicipio(MunicipioEntity pMunicipioEntity);

    List<AsentamientoEntity> listAllByMunicipio(MunicipioEntity pMunicipioEntity);
}
