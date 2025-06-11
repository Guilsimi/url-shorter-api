package com.example.ls_api.services.exceptions.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.ls_api.model.dto.UrlErrorResponse;
import com.example.ls_api.services.exceptions.UrlShortenerException;

@ControllerAdvice
public class InvalidUrlException {

    @ExceptionHandler(UrlShortenerException.class)
    public ResponseEntity<UrlErrorResponse> handle(UrlShortenerException exception) {
        UrlErrorResponse response = new UrlErrorResponse(exception.getUrl(), exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}
