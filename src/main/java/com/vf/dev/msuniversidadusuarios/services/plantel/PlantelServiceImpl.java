package com.vf.dev.msuniversidadusuarios.services.plantel;

import com.vf.dev.msuniversidadusuarios.model.dto.ComboDTO;
import com.vf.dev.msuniversidadusuarios.model.entity.EstadoEntity;
import com.vf.dev.msuniversidadusuarios.model.entity.PlantelEntity;
import com.vf.dev.msuniversidadusuarios.repository.IPlantelRepository;
import com.vf.dev.msuniversidadusuarios.utils.exception.MsUniversidadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PlantelServiceImpl implements IPlantelService {
    @Autowired
    private IPlantelRepository iPlantelRepository;

    @Override
    public List<ComboDTO> listPlateles(EstadoEntity pEstadoEntity) {
        List<PlantelEntity> mPlantelEntityList = this.iPlantelRepository.listPlanteles(pEstadoEntity);
        List<ComboDTO> mComboDTOList = new ArrayList<>();
        if (mPlantelEntityList != null && !mPlantelEntityList.isEmpty()) {
            mPlantelEntityList.forEach(p -> {
                mComboDTOList.add(
                        ComboDTO.builder()
                                .value(p.getIdPlantel().toString())
                                .label(p.getNombre())
                                .build()
                );
            });
        }
        return mComboDTOList;
    }

    @Override
    public PlantelEntity findById(Integer pIdPlantel) throws MsUniversidadException {
        Optional<PlantelEntity> mPlantelEntityOptional = this.iPlantelRepository.findById(pIdPlantel);
        if (mPlantelEntityOptional.isPresent()) {
            return mPlantelEntityOptional.get();
        } else {
            throw new MsUniversidadException("No se encontro el plantel con el id :" + pIdPlantel, "");
        }
    }

    @Override
    public List<PlantelEntity> findAllByMunicipio(EstadoEntity pEstadoEntity) {
       return this.iPlantelRepository.listPlanteles(pEstadoEntity);
    }
}
