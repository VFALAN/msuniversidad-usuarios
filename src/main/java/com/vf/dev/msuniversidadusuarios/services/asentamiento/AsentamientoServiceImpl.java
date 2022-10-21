package com.vf.dev.msuniversidadusuarios.services.asentamiento;

import com.vf.dev.msuniversidadusuarios.model.dto.ComboDTO;
import com.vf.dev.msuniversidadusuarios.model.entity.AsentamientoEntity;
import com.vf.dev.msuniversidadusuarios.model.entity.MunicipioEntity;
import com.vf.dev.msuniversidadusuarios.repository.IAsentamientoRepository;
import com.vf.dev.msuniversidadusuarios.utils.exception.MsUniversidadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AsentamientoServiceImpl implements IAsentamientoService {
    @Autowired
    private IAsentamientoRepository iAsentamientoRepository;

    @Override
    public AsentamientoEntity findById(Integer pAsentamientoId) throws MsUniversidadException {
        Optional<AsentamientoEntity> mOptionalAsentamientoEntity = this.iAsentamientoRepository.findById(pAsentamientoId);
        if (mOptionalAsentamientoEntity.isPresent()) {
            return mOptionalAsentamientoEntity.get();
        } else {
            throw new MsUniversidadException("No se encuentra el asentameinto con el Id :" + pAsentamientoId, "EL0001");
        }
    }

    @Override
    public List<ComboDTO> listByMunicipio(MunicipioEntity pMunicipioEntity) {
        List<ComboDTO> mComboList = new ArrayList<>();
        List<AsentamientoEntity> mAsentamientoList = this.iAsentamientoRepository.findAllByMunicipio(pMunicipioEntity);
        if(!mAsentamientoList.isEmpty()){
            mAsentamientoList.forEach( m ->{
                mComboList.add(ComboDTO.builder().value(m.getIdAsentamientos().toString()).label(m.getCodigoPostal() + " / " + m.getNombre().toUpperCase()).build());
            });
        }
        return mComboList;
    }
}
