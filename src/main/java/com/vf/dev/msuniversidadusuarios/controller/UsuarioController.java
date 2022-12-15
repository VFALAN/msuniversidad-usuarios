package com.vf.dev.msuniversidadusuarios.controller;

import com.google.gson.Gson;
import com.vf.dev.msuniversidadusuarios.model.dto.DisponivilidadResponseDTO;
import com.vf.dev.msuniversidadusuarios.model.dto.PaginationObject;
import com.vf.dev.msuniversidadusuarios.model.dto.usuario.UsuarioDTO;
import com.vf.dev.msuniversidadusuarios.model.dto.usuario.UsuarioTableDTO;
import com.vf.dev.msuniversidadusuarios.model.entity.UsuarioEntity;
import com.vf.dev.msuniversidadusuarios.services.archivos.IArchivosService;
import com.vf.dev.msuniversidadusuarios.services.usuario.IUsuarioService;
import com.vf.dev.msuniversidadusuarios.utils.exception.MsUniversidadException;
import com.vf.dev.msuniversidadusuarios.utils.cosnts.EBucket;
import com.vf.dev.msuniversidadusuarios.utils.cosnts.ETipoArchivo;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping
    public ResponseEntity<?> list(@RequestParam(name = "size", defaultValue = "10") Integer pSize,
                                  @RequestParam(name = "page", defaultValue = "0") Integer pPage,
                                  @RequestParam(name = "column", defaultValue = "idUsuario") String pColumn,
                                  @RequestParam(name = "order", defaultValue = "desc") String pOrden,
                                  @RequestParam(name = "filters", required = false) String pFilters) {
        Map<String, Object> map = pFilters != null ? new Gson().fromJson(pFilters, Map.class) : null;
        PaginationObject<UsuarioTableDTO> mPaginacionObjectUsuario = this.iUsuarioService.paginar(pSize, pPage, pColumn, pOrden, map);
        return new ResponseEntity<PaginationObject<UsuarioTableDTO>>(mPaginacionObjectUsuario, HttpStatus.OK);

    }

    @GetMapping("/{pIdUsuario}")
    public ResponseEntity<?> findById(@PathVariable Integer pIdUsuario) throws MsUniversidadException {
            UsuarioDTO musuarioDto = this.iUsuarioService.getDetail(pIdUsuario);
            return new ResponseEntity<UsuarioDTO>(musuarioDto, HttpStatus.OK);


    }

    @PostMapping
    public ResponseEntity<?> save(@RequestParam("usuario") String pUsuarioJsonStr,
                                  @RequestParam("fotografia") MultipartFile pArchivoFoto,
                                  @RequestParam("curp") MultipartFile pArchivoCurp,
                                  @RequestParam("comprobante") MultipartFile pComprobante,
                                  @RequestParam("actaNacimiento") MultipartFile pActaNacimiento) throws MsUniversidadException {

            Gson gson = new Gson();
            UsuarioDTO mUsuarioDTO = gson.fromJson(pUsuarioJsonStr, UsuarioDTO.class);
            UsuarioEntity mUsuarioEntity = this.iUsuarioService.saveFromDto(mUsuarioDTO);
            this.iArchivosService.guardarArchivo(pArchivoFoto, ETipoArchivo.FOTOGRAFIA_REGISTRO.getId(), EBucket.BUCKETS_INFORMACION_PERSONAL.getId(), mUsuarioEntity);
            this.iArchivosService.guardarArchivo(pArchivoCurp, ETipoArchivo.CURP.getId(), EBucket.BUCKETS_INFORMACION_PERSONAL.getId(), mUsuarioEntity);
            this.iArchivosService.guardarArchivo(pComprobante, ETipoArchivo.COMPROBANTE_DOMICILIO.getId(), EBucket.BUCKETS_INFORMACION_PERSONAL.getId(), mUsuarioEntity);
            this.iArchivosService.guardarArchivo(pActaNacimiento, ETipoArchivo.ACTA_DE_NACIMINETO.getId(), EBucket.BUCKETS_INFORMACION_PERSONAL.getId(), mUsuarioEntity);
            mUsuarioDTO = this.iUsuarioService.getDetail(mUsuarioEntity.getIdUsuario());
            return new ResponseEntity<UsuarioDTO>(mUsuarioDTO, HttpStatus.OK);

    }


    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<?> delete(@PathVariable Integer idUsuario) throws MsUniversidadException {

            UsuarioEntity mUsuarioEntity = this.iUsuarioService.findById(idUsuario);
            this.iUsuarioService.delete(mUsuarioEntity);
            return new ResponseEntity<>(true, HttpStatus.OK);

    }


    @PutMapping("/disabled")
    public ResponseEntity<?> disabeld(@RequestParam("idUsuario") Integer pIdUsuario) throws MsUniversidadException {

            UsuarioEntity mUsuarioEntity = this.iUsuarioService.findById(pIdUsuario);

            mUsuarioEntity.setIndActivo(!mUsuarioEntity.getIndActivo());
            if (!mUsuarioEntity.getIndActivo()) {
                mUsuarioEntity.setFechaBaja(null);
                mUsuarioEntity.setFechaActualizacion(new Date());
            } else {
                mUsuarioEntity.setFechaBaja(new Date());
            }
            mUsuarioEntity = this.iUsuarioService.save(mUsuarioEntity);
            return new ResponseEntity<Integer>(pIdUsuario, HttpStatus.OK);

    }


    @PutMapping("/{idUsuario}")
    public ResponseEntity<?> update(@PathVariable Integer idUsuario, @RequestBody UsuarioDTO usuarioDTO) {
        return null;
    }

    @GetMapping("/validarUsuario")
    public ResponseEntity<?> validarUsuario(@RequestParam("nombreUsuario") String pNombreUsuario, @RequestParam("correo") String pCorreo) {
        DisponivilidadResponseDTO mDisponivilidadResponseDTO = this.iUsuarioService.validarUsernameAndCorreo(pNombreUsuario, pCorreo);
        return new ResponseEntity<DisponivilidadResponseDTO>(mDisponivilidadResponseDTO, HttpStatus.OK);
    }
}

