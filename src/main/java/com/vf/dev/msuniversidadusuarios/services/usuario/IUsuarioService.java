package com.vf.dev.msuniversidadusuarios.services.usuario;

import com.vf.dev.msuniversidadusuarios.model.dto.usuario.UsuarioDTO;
import com.vf.dev.msuniversidadusuarios.model.dto.usuario.UsuarioTableDTO;
import com.vf.dev.msuniversidadusuarios.model.entity.UsuarioEntity;
import com.vf.dev.msuniversidadusuarios.services.generic.IGenericService;

public interface IUsuarioService extends IGenericService<UsuarioEntity, UsuarioDTO, UsuarioTableDTO> {
}
