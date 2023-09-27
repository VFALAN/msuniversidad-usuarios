package com.vf.dev.msuniversidadusuarios.services.msexcelexport;

import com.vf.dev.msuniversidadusuarios.model.dto.ExportUsuarioListRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "MSUNIVERSIDAD-EXCEL-BUILDER")
public interface IMsExcelExport {
    @Async
    @PostMapping(value = "/msuniversidad-excel-builder/api/export/usuarioDataTable")
     void exportUsuarioDataTable(@RequestBody ExportUsuarioListRequestDTO usuarioDTO);
}
