package com.vf.dev.msuniversidadusuarios.services.carreras;

import com.vf.dev.msuniversidadusuarios.model.dto.ComboDTO;
import com.vf.dev.msuniversidadusuarios.model.entity.CarreraEntity;
import com.vf.dev.msuniversidadusuarios.model.entity.PlantelEntity;
import com.vf.dev.msuniversidadusuarios.repository.ICarreraRepository;
import com.vf.dev.msuniversidadusuarios.utils.exception.MsUniversidadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CarreraServiceImpl implements ICarreraService {
    @Autowired
    private ICarreraRepository iCarreraRepository;

    @Override
    public CarreraEntity findById(Integer pIdCarrera) throws MsUniversidadException {
        Optional<CarreraEntity> mCarreraEntityOptional = this.iCarreraRepository.findById(pIdCarrera);
        if (mCarreraEntityOptional.isPresent()) {
            return mCarreraEntityOptional.get();
        } else {
            throw new MsUniversidadException("no se encontro la carrera con el id: " + pIdCarrera, "");
        }
    }

    @Override
    public List<ComboDTO> listByPlantel(PlantelEntity plantelEntity) {
        List<ComboDTO> mComboDTOList = new ArrayList<>();
        List<CarreraEntity> mCarreraEntityList = this.iCarreraRepository.findByPlantel(plantelEntity);
        if (mCarreraEntityList != null && !mCarreraEntityList.isEmpty()) {
            mCarreraEntityList.forEach(c -> {
                mComboDTOList.add(ComboDTO.builder()
                        .label(c.getNombre())
                        .value(c.getIdCarrera().toString())
                        .build());
            });
        }

        return mComboDTOList;
    }
}
