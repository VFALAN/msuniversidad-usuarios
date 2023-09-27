package com.vf.dev.msuniversidadusuarios.utils;

import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class HttpServletUtil {

    public static String requestData(HttpServletRequest request) {
        JsonObject jsonError = new JsonObject();
        JsonObject jsonHeaders = new JsonObject();
        JsonObject paramsJson = new JsonObject();
        jsonError.addProperty("method",request.getMethod());
        jsonError.addProperty("url",request.getRequestURL().toString());



        List<String> headers = Collections.list(request.getHeaderNames());
        List<String> params = Collections.list((request.getParameterNames()));

        for (String headerName : headers) {
            jsonHeaders.addProperty(headerName,request.getHeader(headerName));
        }

        for (String paramName : params) {
            paramsJson.addProperty(paramName,request.getParameter(paramName));


        }
        jsonError.add("headers",jsonHeaders);
        jsonError.add("params",paramsJson);
        jsonError.addProperty("fecha",new Date().getTime());
        return jsonError.toString();
    }
}
