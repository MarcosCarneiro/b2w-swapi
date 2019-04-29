package com.marcos.lima.b2wswapi.controllers.exception;

public class UntitledPlanet extends RuntimeException {

    public UntitledPlanet(){
        super("Planet should be have a name");
    }
}
