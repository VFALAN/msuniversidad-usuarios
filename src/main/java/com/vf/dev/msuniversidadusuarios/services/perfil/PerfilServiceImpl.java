package com.vf.dev.msuniversidadusuarios.services.perfil;

import com.vf.dev.msuniversidadusuarios.model.entity.PerfilEntity;
import com.vf.dev.msuniversidadusuarios.repository.IPerfilRespository;
import com.vf.dev.msuniversidadusuarios.utils.exception.MsUniversidadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PerfilServiceImpl implements IPerfilService {
    @Autowired
    private IPerfilRespository iPerfilRespository;


    @Override
    public PerfilEntity findById(Integer pIdPerfil) throws MsUniversidadException {
        Optional<PerfilEntity> mOptionalPerfilEntity = this.iPerfilRespository.findById(pIdPerfil);
        if (mOptionalPerfilEntity.isPresent()) {
            return mOptionalPerfilEntity.get();
        } else {
            throw new MsUniversidadException("No se encontron ningun Perfil con el Id: " + pIdPerfil, "EL00001");
        }

    }
}
