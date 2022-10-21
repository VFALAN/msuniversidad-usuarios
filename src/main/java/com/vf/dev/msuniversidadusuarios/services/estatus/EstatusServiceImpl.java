package com.vf.dev.msuniversidadusuarios.services.estatus;

import com.vf.dev.msuniversidadusuarios.model.entity.EstatusEntity;
import com.vf.dev.msuniversidadusuarios.repository.IEstatusRepository;
import com.vf.dev.msuniversidadusuarios.utils.exception.MsUniversidadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EstatusServiceImpl implements IEstatusService {
    @Autowired
    private IEstatusRepository iEstatusRepository;

    @Override
    public EstatusEntity findById(Integer pEstatusId) throws MsUniversidadException {
        Optional<EstatusEntity> mOptionalEstatusEntity = this.iEstatusRepository.findById(pEstatusId);
        if (mOptionalEstatusEntity.isPresent()) {
            return mOptionalEstatusEntity.get();
        } else {
            throw new MsUniversidadException("No se encontro ningun estatus con el id: " + pEstatusId, "EL0001");
        }
    }
}
