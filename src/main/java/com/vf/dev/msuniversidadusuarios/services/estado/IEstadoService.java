package com.vf.dev.msuniversidadusuarios.services.estado;

import com.vf.dev.msuniversidadusuarios.model.dto.ComboDTO;
import com.vf.dev.msuniversidadusuarios.model.entity.EstadoEntity;
import com.vf.dev.msuniversidadusuarios.utils.exception.MsUniversidadException;

import java.util.List;

public interface IEstadoService {
    EstadoEntity findById(Integer pEstadoId) throws MsUniversidadException;

    List<ComboDTO> getAll();
}
