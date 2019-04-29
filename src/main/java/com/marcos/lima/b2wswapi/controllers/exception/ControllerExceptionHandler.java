package com.marcos.lima.b2wswapi.controllers.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(PlanetNotFoundException.class)
    public ResponseEntity<StandardError> planetNotFound(PlanetNotFoundException e){
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError error = new StandardError(System.currentTimeMillis(), status.value(), "Not found", e.getMessage());
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(UntitledPlanet.class)
    public ResponseEntity<StandardError> untitledPlanet(UntitledPlanet e){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        StandardError error = new StandardError(System.currentTimeMillis(), status.value(), "Untitled planet", e.getMessage());
        return ResponseEntity.status(status).body(error);
    }
}
