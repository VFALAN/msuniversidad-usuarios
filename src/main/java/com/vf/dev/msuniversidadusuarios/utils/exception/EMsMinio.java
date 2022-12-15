package com.vf.dev.msuniversidadusuarios.utils.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EMsMinio {
    CONTEXT_PATH("/msuniversidad-minio/api/"),
    CONTROLLER("/file"),
    APLICATION_NAME("MSUNIVERSIDAD-MINIO");
    private String value;
}
