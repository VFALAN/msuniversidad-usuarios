package com.vf.dev.msuniversidadusuarios.services.archivos;

import com.netflix.discovery.EurekaClient;
import com.vf.dev.msuniversidadusuarios.model.entity.UsuarioEntity;
import com.vf.dev.msuniversidadusuarios.services.msminioservice.IMsMinioService;
import com.vf.dev.msuniversidadusuarios.utils.cosnts.EPerfiles;
import com.vf.dev.msuniversidadusuarios.utils.exception.MsUniversidadException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
@Slf4j
public class ArchivoServiceImpl implements IArchivosService {
    @Autowired
    private IMsMinioService iMsMinioService;


    @Override
    public void guardarArchivo(MultipartFile pFile, Integer tipoArchivo, Integer pBucket, UsuarioEntity pUsuario) throws MsUniversidadException {
        String extencion = pFile.getOriginalFilename().substring(pFile.getOriginalFilename().lastIndexOf(".") + 1);
        String ruta = this.buildPath(pUsuario);
        ResponseEntity<String> mResponseMinioserver = this.iMsMinioService.saveFile(pFile, pUsuario.getIdUsuario(), tipoArchivo, pBucket, extencion, ruta);
        if (mResponseMinioserver.getStatusCode() != HttpStatus.OK) {
            throw new MsUniversidadException("Error en el Servidor:    " + mResponseMinioserver.getBody());
        }
    }

    @Override
    public void updateFile(MultipartFile pFile, Integer idArchivo) throws MsUniversidadException {
        var response = this.iMsMinioService.updateFile(idArchivo, pFile);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new MsUniversidadException("Error en el Servidor:    " + response.getBody());
        }
    }

    private String buildPath(UsuarioEntity pUsusarioEntity) {
        String mStringPath = "";
        if (pUsusarioEntity.getIdPlantel() != null) {
            mStringPath += pUsusarioEntity.getIdPlantel().getClave().concat("/");
        }
        mStringPath += pUsusarioEntity.getIdPerfil().getClave().concat("/");
        if (!Objects.equals(pUsusarioEntity.getIdPerfil().getIdPerfil(), EPerfiles.ID_ADMIN) && pUsusarioEntity.getIdCarrera() != null) {
            mStringPath += pUsusarioEntity.getIdCarrera().getNombre().replace(" ", "_").concat("/");
        }
        mStringPath += pUsusarioEntity.getCurp().concat("/");
        return mStringPath;
    }
}
