package com.vf.dev.msuniversidadusuarios.utils;

import java.util.HashMap;
import java.util.Map;

public class FileUtils {
    public static final Map<String, String> extensions;

    // Inicialización estática
    static {
        extensions = new HashMap<>();
        extensions.put("jpg", "image/jpeg");
        extensions.put("jpeg", "image/jpeg");
        extensions.put("png", "image/png");
        extensions.put("gif", "image/gif");
        extensions.put("pdf", "application/pdf");
        extensions.put("doc", "application/msword");
        extensions.put("xls", "application/vnd.ms-excel");
        extensions.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        extensions.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        extensions.put("txt", "text/plain");
        extensions.put("json", "application/json");
        extensions.put("xml", "application/xml");
        extensions.put("mp3", "audio/mpeg");
        extensions.put("mp4", "video/mp4");
    }

    public static final String getExtencionByContentType(String pContentTypeStr) {
        return extensions.getOrDefault(pContentTypeStr, "NotFound");
    }
}
