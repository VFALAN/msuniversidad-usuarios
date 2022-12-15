package com.vf.dev.msuniversidadusuarios.services.carreras;

import com.vf.dev.msuniversidadusuarios.model.dto.ComboDTO;
import com.vf.dev.msuniversidadusuarios.model.entity.CarreraEntity;
import com.vf.dev.msuniversidadusuarios.model.entity.PlantelEntity;
import com.vf.dev.msuniversidadusuarios.utils.exception.MsUniversidadException;

import java.util.List;

public interface ICarreraService {

    CarreraEntity findById(Integer pIdCarrera) throws MsUniversidadException;

    List<ComboDTO> listByPlantel(PlantelEntity plantelEntity);
}
