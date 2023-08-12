package com.vf.dev.msuniversidadusuarios.services.plantel;


import com.vf.dev.msuniversidadusuarios.model.dto.ComboDTO;
import com.vf.dev.msuniversidadusuarios.model.entity.EstadoEntity;
import com.vf.dev.msuniversidadusuarios.model.entity.PlantelEntity;
import com.vf.dev.msuniversidadusuarios.utils.exception.MsUniversidadException;

import java.util.List;

public interface IPlantelService {

    public List<ComboDTO> listPlateles(EstadoEntity pEstadoEntity);

    PlantelEntity findById(Integer pIdPlantel) throws MsUniversidadException;

    List<PlantelEntity> findAllByMunicipio(EstadoEntity pEstadoEntity);
}
