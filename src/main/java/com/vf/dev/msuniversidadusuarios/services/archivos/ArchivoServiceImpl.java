package com.vf.dev.msuniversidadusuarios.services.archivos;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.vf.dev.msuniversidadusuarios.model.entity.UsuarioEntity;
import com.vf.dev.msuniversidadusuarios.services.msminioservice.IMsMinioService;
import com.vf.dev.msuniversidadusuarios.utils.exception.EMsMinio;
import com.vf.dev.msuniversidadusuarios.utils.exception.MsUniversidadException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ArchivoServiceImpl implements IArchivosService {
    @Autowired
    private IMsMinioService iMsMinioService;

    @Autowired
    private EurekaClient eurekaClient;

    @Override
    public String guardarArchivo(MultipartFile pFile, Integer tipoArchivo, Integer pBucket, UsuarioEntity pUsuario) throws MsUniversidadException {
            String extencion = pFile.getOriginalFilename().substring(pFile.getOriginalFilename().lastIndexOf(".") + 1);
            ResponseEntity<String> mResponseMinioserver = this.iMsMinioService.saveFile(pFile,pUsuario.getIdUsuario(),tipoArchivo,pBucket,extencion);
            if (mResponseMinioserver.getStatusCode() == HttpStatus.OK) {
                return mResponseMinioserver.getBody();
            } else {
                throw new MsUniversidadException("Error en el Servidor:    "+ mResponseMinioserver.getBody());
            }

    }


}
