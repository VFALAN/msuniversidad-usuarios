package com.vf.dev.msuniversidadusuarios.services.archivos;

import com.vf.dev.msuniversidadusuarios.model.entity.UsuarioEntity;
import com.vf.dev.msuniversidadusuarios.utils.exception.MsUniversidadException;
import com.vf.dev.msuniversidadusuarios.utils.exception.MsUniversidadNoExtencionException;
import org.springframework.web.multipart.MultipartFile;

public interface IArchivosService {

    void guardarArchivo(MultipartFile pFile, Integer tipoArchivo, Integer Bucket ,UsuarioEntity pUsuario) throws MsUniversidadException, MsUniversidadNoExtencionException;
    void updateFile(MultipartFile pFile,Integer idArchivo) throws MsUniversidadException;

}
