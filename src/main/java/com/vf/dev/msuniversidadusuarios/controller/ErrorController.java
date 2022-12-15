package com.vf.dev.msuniversidadusuarios.controller;

import com.vf.dev.msuniversidadusuarios.utils.HttpServletUtil;
import com.vf.dev.msuniversidadusuarios.utils.exception.MsUniversidadException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@ControllerAdvice
@Slf4j
public class ErrorController {
    @ExceptionHandler({MsUniversidadException.class})
    public ResponseEntity<?> handlerException(MsUniversidadException ex, HttpServletRequest pHttpServletRequest) {
        log.info("Error in bakc" + ex.getMessage());
        log.info(HttpServletUtil.requestData(pHttpServletRequest));
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler({Exception.class})
    public ResponseEntity<?> handlerException(Exception ex, HttpServletRequest pHttpServletRequest) {
        log.info("Error in bakc" + ex.getMessage());
        log.info(HttpServletUtil.requestData(pHttpServletRequest));
        return new ResponseEntity<>("Ha Ocurrido un error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
