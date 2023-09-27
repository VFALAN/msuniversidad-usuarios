package com.vf.dev.msuniversidadusuarios.services.usuario;

import com.vf.dev.msuniversidadusuarios.model.dto.DisponivilidadResponseDTO;
import com.vf.dev.msuniversidadusuarios.model.dto.usuario.IUsuarioDetalle;
import com.vf.dev.msuniversidadusuarios.model.dto.usuario.UsuarioDTO;
import com.vf.dev.msuniversidadusuarios.model.dto.usuario.UsuarioTableDTO;
import com.vf.dev.msuniversidadusuarios.model.entity.UsuarioEntity;
import com.vf.dev.msuniversidadusuarios.services.generic.IGenericService;
import com.vf.dev.msuniversidadusuarios.utils.exception.MsUniversidadException;
import org.springframework.web.multipart.MultipartFile;

public interface IUsuarioService extends IGenericService<UsuarioEntity, UsuarioDTO, UsuarioTableDTO, IUsuarioDetalle> {
    DisponivilidadResponseDTO validarUsernameAndCorreo(String pUsername, String pCorreo);
     String exportatInformacion(String pFilters, String pOrders, Integer pIdUSuario) throws MsUniversidadException;
}
