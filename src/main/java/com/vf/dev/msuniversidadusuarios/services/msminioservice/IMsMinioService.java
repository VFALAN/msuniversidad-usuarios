package com.vf.dev.msuniversidadusuarios.services.msminioservice;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;


@FeignClient(value = "MSUNIVERSIDAD-MINIO")
public interface IMsMinioService {
    @PostMapping(value = "/msuniversidad-minio/api/file", consumes = {"multipart/form-data"})
    ResponseEntity<String> saveFile(@RequestPart("file") MultipartFile pFile,
                                    @RequestParam("idUsuario") Integer idUsuario,
                                    @RequestParam("tipoArchivo") Integer pTipoArchivo,
                                    @RequestParam("idBucket") Integer pIdBucket,
                                    @RequestParam("extencion") String pExtencion,
                                    @RequestParam("ruta") String pRuta
    );
    @PutMapping(value  ="/msuniversidad-minio/api/file" , consumes = {"multipart/form-data"})
    ResponseEntity<String>updateFile(@RequestParam("idArchivo")Integer idArchivo,@RequestParam("file")MultipartFile pMultipartFile);
}
