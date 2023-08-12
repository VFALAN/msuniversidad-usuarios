package com.vf.dev.msuniversidadusuarios.services.direccion;

import com.vf.dev.msuniversidadusuarios.model.dto.PaginationObject;
import com.vf.dev.msuniversidadusuarios.model.dto.direccion.DireccionDTO;
import com.vf.dev.msuniversidadusuarios.model.dto.direccion.DireccionTableDTO;
import com.vf.dev.msuniversidadusuarios.model.entity.DireccionEntity;
import com.vf.dev.msuniversidadusuarios.repository.IDireccionRespoitory;
import com.vf.dev.msuniversidadusuarios.utils.exception.MsUniversidadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DireccionServiceImpl implements IDireccionService {
    @Autowired
    private IDireccionRespoitory iDireccionRespoitory;


    @Override
    public DireccionEntity findById(Integer pId) throws MsUniversidadException {
        Optional<DireccionEntity> mOptionalEntity = this.iDireccionRespoitory.findById(pId);
        if (mOptionalEntity.isPresent()) {
            return mOptionalEntity.get();
        } else {
            throw new MsUniversidadException("No se encontro ninguna direccion con el id: " + pId, "EL0001");
        }
    }

    @Override
    public DireccionEntity saveFromDto(DireccionDTO dto) {
        return null;
    }

    @Override
    public DireccionEntity save(DireccionEntity pEntity) {
        return this.iDireccionRespoitory.save(pEntity);
    }

    @Override
    public void delete(DireccionEntity entity) {
        entity.setFechaBaja(new Date());
        entity.setIndActivo(false);
        this.save(entity);

    }

    @Override
    public DireccionEntity update(DireccionEntity entity) {
        entity.setFechaActualizacion(new Date());
        return this.save(entity);
    }

    @Override
    public List<DireccionEntity> finaAll() {
        return null;
    }

    @Override
    public DireccionDTO getDetail(Integer pId) throws MsUniversidadException {
        return null;
    }

    @Override
    public PaginationObject<DireccionTableDTO> paginar(int size, int page, String column, Map<String , Object>order, Map<String , Object> filters) {
        return null;
    }
}
