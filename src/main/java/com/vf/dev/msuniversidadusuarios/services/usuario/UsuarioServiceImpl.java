package com.vf.dev.msuniversidadusuarios.services.usuario;

import com.vf.dev.msuniversidadusuarios.model.dto.PaginationObject;
import com.vf.dev.msuniversidadusuarios.model.dto.usuario.UsuarioDTO;
import com.vf.dev.msuniversidadusuarios.model.dto.usuario.UsuarioTableDTO;
import com.vf.dev.msuniversidadusuarios.model.entity.*;
import com.vf.dev.msuniversidadusuarios.repository.IUsuarioRepository;
import com.vf.dev.msuniversidadusuarios.services.asentamiento.IAsentamientoService;
import com.vf.dev.msuniversidadusuarios.services.direccion.IDireccionService;
import com.vf.dev.msuniversidadusuarios.services.estatus.IEstatusService;
import com.vf.dev.msuniversidadusuarios.services.perfil.IPerfilService;
import com.vf.dev.msuniversidadusuarios.utils.exception.MsUniversidadException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements IUsuarioService {
    @Autowired
    private IUsuarioRepository iUsuarioRepository;
    @Autowired
    private IDireccionService iDireccionService;
    @Autowired
    private IPerfilService iPerfilService;
    @Autowired
    private IEstatusService iEstatusService;
    @Autowired
    private IAsentamientoService iAsentamientoService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public UsuarioEntity findById(Integer pId) throws MsUniversidadException {
        Optional<UsuarioEntity> optionalUsuarioEntity = this.iUsuarioRepository.findById(pId);
        if (optionalUsuarioEntity.isPresent()) {
            return optionalUsuarioEntity.get();
        } else {
            throw new MsUniversidadException("No Se encontro ningun Usuario con el Id: " + pId, "ED001");
        }

    }

    @Override
    public UsuarioEntity saveFromDto(UsuarioDTO dto) throws MsUniversidadException {
        UsuarioEntity mUsuarioEntity = modelMapper.map(dto, UsuarioEntity.class);
        DireccionEntity mDireccionEntity = modelMapper.map(dto, DireccionEntity.class);
        EstatusEntity mEstatusEntity = this.iEstatusService.findById(dto.getIdEstatus());
        PerfilEntity mPerfilEntity = this.iPerfilService.findById(dto.getIdPerfil());
        AsentamientoEntity mAsentamientoEntity = this.iAsentamientoService.findById(dto.getIdAsentamiento());
        mDireccionEntity.setFechaAlta(new Date());
        mDireccionEntity.setIndActivo(true);
        mDireccionEntity.setIdAsentamiento(mAsentamientoEntity);
        mDireccionEntity = this.iDireccionService.save(mDireccionEntity);
        mUsuarioEntity.setEstatus(mEstatusEntity);
        mUsuarioEntity.setPerfil(mPerfilEntity);
        mUsuarioEntity.setPassword(this.passwordEncoder.encode(dto.getPassword()));
        mUsuarioEntity.setIndActivo(true);
        mUsuarioEntity.setFechaAlta(new Date());
        mUsuarioEntity.setIdDireccion(mDireccionEntity);
        return this.save(mUsuarioEntity);
    }

    @Override
    public UsuarioEntity save(UsuarioEntity pEntity) {
        return this.iUsuarioRepository.save(pEntity);
    }

    @Override
    public void delete(UsuarioEntity entity) {
        entity.setIndActivo(false);
        entity.setFechaBaja(new Date());
        this.save(entity);
    }

    @Override
    public UsuarioEntity update(UsuarioEntity entity) {
        entity.setFechaActualizacion(new Date());
        return this.save(entity);
    }

    @Override
    public List<UsuarioEntity> finaAll() {
        return this.iUsuarioRepository.findAll();
    }

    @Override
    public PaginationObject<UsuarioTableDTO> paginar(int size, int page, String column, String orden) {
        return null;
    }
}
