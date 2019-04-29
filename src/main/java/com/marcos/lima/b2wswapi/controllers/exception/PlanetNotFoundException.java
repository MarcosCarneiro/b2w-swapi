package com.marcos.lima.b2wswapi.controllers.exception;

public class PlanetNotFoundException extends RuntimeException {

    public PlanetNotFoundException(String id){
        super(String.format("Planet [%s]. Not found", id));
    }
}
