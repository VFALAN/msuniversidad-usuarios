package com.vf.dev.msuniversidadusuarios.controller;

import com.vf.dev.msuniversidadusuarios.model.dto.ComboDTO;
import com.vf.dev.msuniversidadusuarios.model.entity.EstadoEntity;
import com.vf.dev.msuniversidadusuarios.model.entity.MunicipioEntity;
import com.vf.dev.msuniversidadusuarios.model.entity.PlantelEntity;
import com.vf.dev.msuniversidadusuarios.services.asentamiento.IAsentamientoService;
import com.vf.dev.msuniversidadusuarios.services.carreras.ICarreraService;
import com.vf.dev.msuniversidadusuarios.services.estado.IEstadoService;
import com.vf.dev.msuniversidadusuarios.services.municipio.IMunicipioService;
import com.vf.dev.msuniversidadusuarios.services.plantel.IPlantelService;
import com.vf.dev.msuniversidadusuarios.utils.exception.MsUniversidadException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalogos")
@CrossOrigin(origins = "*")
@Slf4j
public class CatalogoController {
    @Autowired
    private IEstadoService iEstadoService;
    @Autowired
    private IMunicipioService iMunicipioService;
    @Autowired
    private IAsentamientoService iAsentamientoService;
    @Autowired
    private IPlantelService iPlantelService;

    @Autowired
    private ICarreraService iCarreraService;

    @GetMapping("/estados")
    ResponseEntity<?> getEstados() {
        List<ComboDTO> mComboList = this.iEstadoService.getAll();
        return new ResponseEntity<List<ComboDTO>>(mComboList, HttpStatus.OK);
    }

    @GetMapping("/municipios/{pEstadoId}")
    ResponseEntity<?> getMunicipios(@PathVariable Integer pEstadoId) throws MsUniversidadException {

            EstadoEntity mEstadoEntity = this.iEstadoService.findById(pEstadoId);
            List<ComboDTO> mComboDTOList = this.iMunicipioService.findByestado(mEstadoEntity);
            return new ResponseEntity<List<ComboDTO>>(mComboDTOList, HttpStatus.OK);

    }

    @GetMapping("/asentamientos/{pIdMunicipio}")
    ResponseEntity<?> getAsentamientos(@PathVariable Integer pIdMunicipio) throws MsUniversidadException {

            MunicipioEntity mMunicipioEntity = this.iMunicipioService.findById(pIdMunicipio);
            List<ComboDTO> mComboDTOList = this.iAsentamientoService.listByMunicipio(mMunicipioEntity);
            return new ResponseEntity<List<ComboDTO>>(mComboDTOList, HttpStatus.OK);

    }

    @GetMapping("/planteles/{pIdEstado}")
    ResponseEntity<?> getPlanteles(@PathVariable Integer pIdEstado) throws MsUniversidadException {
        EstadoEntity mEstadoEntity = this.iEstadoService.findById(pIdEstado);
        List<ComboDTO> mComboDTOList = this.iPlantelService.listPlateles(mEstadoEntity);
        return new ResponseEntity<>(mComboDTOList, HttpStatus.OK);
    }

    @GetMapping("/carreras/{pIdPlantel}")
    ResponseEntity<?> getCarreras(@PathVariable Integer pIdPlantel) throws MsUniversidadException {
        PlantelEntity mPlantel = this.iPlantelService.findById(pIdPlantel);
        List<ComboDTO> mComboDTOList = this.iCarreraService.listByPlantel(mPlantel);
        return new ResponseEntity<>(mComboDTOList, HttpStatus.OK);
    }
}
