package com.vf.dev.msuniversidadusuarios.controller;

import com.vf.dev.msuniversidadusuarios.model.dto.usuario.UsuarioDTO;
import com.vf.dev.msuniversidadusuarios.model.entity.DireccionEntity;
import com.vf.dev.msuniversidadusuarios.model.entity.UsuarioEntity;
import com.vf.dev.msuniversidadusuarios.repository.IDireccionRespoitory;
import com.vf.dev.msuniversidadusuarios.services.direccion.IDireccionService;
import com.vf.dev.msuniversidadusuarios.services.usuario.IUsuarioService;
import com.vf.dev.msuniversidadusuarios.utils.exception.MsUniversidadException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/usuarios")
@Slf4j
public class UsuarioController {
    @Autowired
    private IUsuarioService iUsuarioService;
    @Autowired
    private IDireccionService iDireccionService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/{idUsuario}")
    public ResponseEntity<?> findById(@PathVariable Integer idUsuario) {
        return null;
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody UsuarioDTO pUsuarioDto) {
        try {
            UsuarioEntity mUsuarioEntity = this.iUsuarioService.saveFromDto(pUsuarioDto);
// todo crear un objeto de respuesta para la entidad sin sobre cargar data
            return new ResponseEntity<UsuarioEntity>(mUsuarioEntity, HttpStatus.OK);
        } catch (MsUniversidadException e) {
            log.info(e.getMessage());
            return new ResponseEntity<String>("Algo salio mal", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<?> delete(@PathVariable Integer idUsuario) {
        return null;
    }

    @PutMapping("/{idUsuario}")
    public ResponseEntity<?> update(@PathVariable Integer idUsuario, @RequestBody UsuarioDTO usuarioDTO) {
        return null;
    }
}

