package com.vf.dev.msuniversidadusuarios.services.estado;

import com.vf.dev.msuniversidadusuarios.model.dto.ComboDTO;
import com.vf.dev.msuniversidadusuarios.model.entity.EstadoEntity;
import com.vf.dev.msuniversidadusuarios.repository.IEstadoRepository;
import com.vf.dev.msuniversidadusuarios.utils.exception.MsUniversidadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EstasdoServiceImpl implements IEstadoService {
    @Autowired
    private IEstadoRepository iEstadoRepository;

    @Override
    public EstadoEntity findById(Integer pEstadoId) throws MsUniversidadException {
        Optional<EstadoEntity> mOptionalEntity = this.iEstadoRepository.findById(pEstadoId);
        if (mOptionalEntity.isPresent()) {
            return mOptionalEntity.get();
        } else {
            throw new MsUniversidadException("No se encontro ningun estado con el id: " + pEstadoId, "EL0001");
        }
    }

    @Override
    public List<ComboDTO> getAll() {
        List<EstadoEntity> mEstadosList = this.iEstadoRepository.findAll();
        List<ComboDTO> mComboList = new ArrayList<>();
        if (!mEstadosList.isEmpty()) {
            mEstadosList.forEach(m -> {
                mComboList.add(ComboDTO.builder().value(m.getIdEstado().toString()).label(m.getNombre().toUpperCase()).build());
            });
            return mComboList;
        } else {
            return null;
        }

    }

    @Override
    public List<EstadoEntity> getAllEstados() {
        return this.iEstadoRepository.findAll();
    }
}
