package com.vf.dev.msuniversidadusuarios.services.municipio;

import com.vf.dev.msuniversidadusuarios.model.dto.ComboDTO;
import com.vf.dev.msuniversidadusuarios.model.entity.EstadoEntity;
import com.vf.dev.msuniversidadusuarios.model.entity.MunicipioEntity;
import com.vf.dev.msuniversidadusuarios.repository.IMunicipioRepository;
import com.vf.dev.msuniversidadusuarios.utils.exception.MsUniversidadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MunicipioServiceImpl implements IMunicipioService {
    @Autowired
    private IMunicipioRepository iMunicipioRepository;

    @Override
    public MunicipioEntity findById(Integer pMuncipioId) throws MsUniversidadException {
        Optional<MunicipioEntity> mOptionalMunicipioEntity = this.iMunicipioRepository.findById(pMuncipioId);
        if (mOptionalMunicipioEntity.isPresent()) {
            return mOptionalMunicipioEntity.get();
        } else {
            throw new MsUniversidadException("No se encontro ningun municipio con el Id: " + pMuncipioId, "EL001");
        }
    }

    @Override
    public List<ComboDTO> findByestado(EstadoEntity pEstadoEntity) {
        List<ComboDTO> mCombolist = new ArrayList<>();
        List<MunicipioEntity> mMunicipiosList = this.iMunicipioRepository.findllByEstado(pEstadoEntity);
        if (!mMunicipiosList.isEmpty()) {
            mMunicipiosList.forEach(m -> {
                mCombolist.add(ComboDTO.builder().value(m.getIdMunicipio().toString()).label(m.getNombre().toUpperCase()).build());
            });
        }
        return mCombolist;
    }
}
