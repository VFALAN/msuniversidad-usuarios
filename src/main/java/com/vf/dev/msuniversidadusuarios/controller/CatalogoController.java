package com.vf.dev.msuniversidadusuarios.controller;

import com.vf.dev.msuniversidadusuarios.model.dto.ComboDTO;
import com.vf.dev.msuniversidadusuarios.model.entity.EstadoEntity;
import com.vf.dev.msuniversidadusuarios.model.entity.MunicipioEntity;
import com.vf.dev.msuniversidadusuarios.services.asentamiento.IAsentamientoService;
import com.vf.dev.msuniversidadusuarios.services.estado.IEstadoService;
import com.vf.dev.msuniversidadusuarios.services.municipio.IMunicipioService;
import com.vf.dev.msuniversidadusuarios.utils.exception.MsUniversidadException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/catalogos")
@Slf4j
public class CatalogoController {
    @Autowired
    private IEstadoService iEstadoService;
    @Autowired
    private IMunicipioService iMunicipioService;
    @Autowired
    private IAsentamientoService iAsentamientoService;


    @GetMapping("/estados")
    ResponseEntity<?> getEstados() {
        List<ComboDTO> mComboList = this.iEstadoService.getAll();
        return new ResponseEntity<List<ComboDTO>>(mComboList, HttpStatus.OK);
    }

    @GetMapping("/municipios/{pEstadoId}")
    ResponseEntity<?> getMunicipios(@PathVariable Integer pEstadoId) {
        try {
            EstadoEntity mEstadoEntity = this.iEstadoService.findById(pEstadoId);
            List<ComboDTO> mComboDTOList = this.iMunicipioService.findByestado(mEstadoEntity);
            return new ResponseEntity<List<ComboDTO>>(mComboDTOList, HttpStatus.OK);
        } catch (MsUniversidadException e) {
            log.info(e.getMessage());
            return new ResponseEntity<String>("Algo salio Mal", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/asentamientos/{pIdMunicipio}")
    ResponseEntity<?> getAsentamientos(@PathVariable Integer pIdMunicipio) {
        try {
            MunicipioEntity mMunicipioEntity = this.iMunicipioService.findById(pIdMunicipio);
            List<ComboDTO> mComboDTOList = this.iAsentamientoService.listByMunicipio(mMunicipioEntity);
            return new ResponseEntity<List<ComboDTO>>(mComboDTOList, HttpStatus.OK);
        } catch (MsUniversidadException e) {
            log.info(e.getMessage());
            return new ResponseEntity<String>("Algo salio Mal", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
