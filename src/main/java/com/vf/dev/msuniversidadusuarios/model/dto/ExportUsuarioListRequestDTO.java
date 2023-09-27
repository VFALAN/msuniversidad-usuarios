package com.vf.dev.msuniversidadusuarios.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vf.dev.msuniversidadusuarios.model.dto.usuario.UsuarioDTO;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExportUsuarioListRequestDTO implements Serializable {
    @JsonProperty("idUsuario")
    private Integer idUsuario;
    @JsonProperty("usuario")
    private List<UsuarioDTO> usuarioList;
}
