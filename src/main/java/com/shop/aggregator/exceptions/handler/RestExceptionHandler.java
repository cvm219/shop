package com.shop.aggregator.exceptions.handler;

import com.shop.aggregator.exceptions.RestException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {
    
    @ExceptionHandler({RestException.class})
    public ResponseEntity handleRestException(RestException e, HttpServletResponse response) {
        response.setStatus(e.getStatusCode());
        ResponseEntity<Object> entity = ResponseEntity.status(e.getStatusCode()).body(e.getDescription());
        return entity;
    }
    
}
