package com.vf.dev.msuniversidadusuarios.controller;

import com.google.gson.Gson;
import com.vf.dev.msuniversidadusuarios.model.dto.DisponivilidadResponseDTO;
import com.vf.dev.msuniversidadusuarios.model.dto.PaginationObject;
import com.vf.dev.msuniversidadusuarios.model.dto.usuario.IUsuarioDetalle;
import com.vf.dev.msuniversidadusuarios.model.dto.usuario.UsuarioDTO;
import com.vf.dev.msuniversidadusuarios.model.dto.usuario.UsuarioTableDTO;
import com.vf.dev.msuniversidadusuarios.model.entity.UsuarioEntity;
import com.vf.dev.msuniversidadusuarios.services.archivos.IArchivosService;
import com.vf.dev.msuniversidadusuarios.services.usuario.IUsuarioService;
import com.vf.dev.msuniversidadusuarios.utils.exception.MsUniversidadException;
import com.vf.dev.msuniversidadusuarios.utils.cosnts.EBucket;
import com.vf.dev.msuniversidadusuarios.utils.cosnts.ETipoArchivo;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.Date;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/usuarios")
@Slf4j
public class UsuarioController {
    @Autowired
    private IUsuarioService iUsuarioService;
    @Autowired
    private IArchivosService iArchivosService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<PaginationObject<UsuarioTableDTO>> list(@RequestParam(name = "size", defaultValue = "10") Integer pSize,
                                                                  @RequestParam(name = "page", defaultValue = "0") Integer pPage,
                                                                  @RequestParam(name = "column", defaultValue = "idUsuario") String pColumn,
                                                                  @RequestParam(name = "order", required = false) String pOrden,
                                                                  @RequestParam(name = "filter", required = false) String pFilters) {
        Map<String, Object> mapFilters = pFilters != null ? new Gson().fromJson(pFilters, Map.class) : null;
        Map<String, Object> mapOrder = pOrden != null ? new Gson().fromJson(pOrden, Map.class) : null;
        PaginationObject<UsuarioTableDTO> mPaginacionObjectUsuario = this.iUsuarioService.paginar(pSize, pPage, pColumn, mapOrder, mapFilters);

        return new ResponseEntity<PaginationObject<UsuarioTableDTO>>(mPaginacionObjectUsuario, HttpStatus.OK);

    }

    @GetMapping("/{pIdUsuario}")
    public ResponseEntity<IUsuarioDetalle> findById(@PathVariable Integer pIdUsuario) throws MsUniversidadException {
        IUsuarioDetalle musuarioDto = this.iUsuarioService.getDetail(pIdUsuario);
        return new ResponseEntity<IUsuarioDetalle>(musuarioDto, HttpStatus.OK);


    }

    @PostMapping
    public ResponseEntity<IUsuarioDetalle> save(@RequestBody @Valid UsuarioDTO pUsusario) throws MsUniversidadException {

        UsuarioEntity mUsuarioEntity = this.iUsuarioService.saveFromDto(pUsusario);
        this.iArchivosService.guardarArchivo(pUsusario.getFotografiaFile(), ETipoArchivo.FOTOGRAFIA_REGISTRO.getId(), EBucket.BUCKETS_INFORMACION_PERSONAL.getId(), mUsuarioEntity);
        this.iArchivosService.guardarArchivo(pUsusario.getCurpFile(), ETipoArchivo.CURP.getId(), EBucket.BUCKETS_INFORMACION_PERSONAL.getId(), mUsuarioEntity);
        this.iArchivosService.guardarArchivo(pUsusario.getComprobanteFile(), ETipoArchivo.COMPROBANTE_DOMICILIO.getId(), EBucket.BUCKETS_INFORMACION_PERSONAL.getId(), mUsuarioEntity);
        this.iArchivosService.guardarArchivo(pUsusario.getActaNacimientoFile(), ETipoArchivo.ACTA_DE_NACIMINETO.getId(), EBucket.BUCKETS_INFORMACION_PERSONAL.getId(), mUsuarioEntity);
        var response = this.iUsuarioService.getDetail(mUsuarioEntity.getIdUsuario());
        return new ResponseEntity<IUsuarioDetalle>(response, HttpStatus.OK);

    }


    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer idUsuario) throws MsUniversidadException {

        UsuarioEntity mUsuarioEntity = this.iUsuarioService.findById(idUsuario);
        this.iUsuarioService.delete(mUsuarioEntity);
        return new ResponseEntity<>(true, HttpStatus.OK);

    }


    @PutMapping("/disabled")
    public ResponseEntity<Integer> disabeld(@RequestParam("idUsuario") Integer pIdUsuario) throws MsUniversidadException {

        UsuarioEntity mUsuarioEntity = this.iUsuarioService.findById(pIdUsuario);

        mUsuarioEntity.setIndActivo(!mUsuarioEntity.getIndActivo());
        if (!mUsuarioEntity.getIndActivo()) {
            mUsuarioEntity.setFechaBaja(null);
            mUsuarioEntity.setFechaActualizacion(new Date());
        } else {
            mUsuarioEntity.setFechaBaja(new Date());
        }
        this.iUsuarioService.save(mUsuarioEntity);
        return new ResponseEntity<Integer>(pIdUsuario, HttpStatus.OK);

    }


    @PutMapping("/{idUsuario}")
    public ResponseEntity<IUsuarioDetalle> update(@PathVariable Integer idUsuario,
                                                  @RequestParam("usuario") String pStrUsaurio,
                                                  @RequestParam(name = "fotoRegistro", required = false) MultipartFile pFotoRegistro,
                                                  @RequestParam(name = "idFotoRegistro", required = false) Integer pIdFotoRegistro,
                                                  @RequestParam(name = "idCurp", required = false) Integer pIdCurp,
                                                  @RequestParam(name = "curp", required = false) MultipartFile pCurp,
                                                  @RequestParam(name = "idActaNaciminto", required = false) Integer pIdActaNacimiento,
                                                  @RequestParam(name = "actaNacimiento", required = false) MultipartFile pActaNacimiento,
                                                  @RequestParam(name = "idComprobanteDocimicilio", required = false) Integer pIdComprobante,
                                                  @RequestParam(name = "comprobanteDomicilio", required = false) MultipartFile pComprobanteDomicilio) throws MsUniversidadException {
        Gson gson = new Gson();
        UsuarioDTO mUsuarioDTO = gson.fromJson(pStrUsaurio, UsuarioDTO.class);
        UsuarioEntity mUsuarioEntity = this.modelMapper.map(mUsuarioDTO, UsuarioEntity.class);
        this.iUsuarioService.update(mUsuarioEntity);
        if (pIdFotoRegistro != null && pFotoRegistro != null) {
            this.iArchivosService.updateFile(pFotoRegistro, pIdFotoRegistro);
        }
        if (pIdActaNacimiento != null && pActaNacimiento != null) {
            this.iArchivosService.updateFile(pActaNacimiento, pIdActaNacimiento);
        }
        if (pIdComprobante != null && pComprobanteDomicilio != null) {
            this.iArchivosService.updateFile(pComprobanteDomicilio, pIdComprobante);
        }
        if (pIdCurp != null && pCurp != null) {
            this.iArchivosService.updateFile(pCurp, pIdCurp);
        }
        var response = this.iUsuarioService.getDetail(idUsuario);
        return new ResponseEntity<IUsuarioDetalle>(response, HttpStatus.OK);
    }

    @GetMapping("/validarUsuario")
    public ResponseEntity<DisponivilidadResponseDTO> validarUsuario(@RequestParam("nombreUsuario") String pNombreUsuario, @RequestParam("correo") String pCorreo) {
        DisponivilidadResponseDTO mDisponivilidadResponseDTO = this.iUsuarioService.validarUsernameAndCorreo(pNombreUsuario, pCorreo);
        return new ResponseEntity<DisponivilidadResponseDTO>(mDisponivilidadResponseDTO, HttpStatus.OK);
    }


    @GetMapping("/export")
    public ResponseEntity<String> exportInfoUsuarios(@RequestParam("filters") String pFilters,
                                                     @RequestParam("order") String pOrder,
                                                     @RequestParam("idUsuario") Integer pIdUsuario) throws MsUniversidadException {

        var response = this.iUsuarioService.exportatInformacion(pFilters, pOrder, pIdUsuario);
        return ResponseEntity.ok(response);
    }
}



