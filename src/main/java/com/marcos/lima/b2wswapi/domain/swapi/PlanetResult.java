package com.marcos.lima.b2wswapi.domain.swapi;

import java.io.Serializable;
import java.util.List;

public class PlanetResult implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<String> films;

    public PlanetResult(){}

    public PlanetResult(List<String> films) {
        this.films = films;
    }

    public List<String> getFilms() {
        return this.films;
    }

    public void setFilms(List<String> films) {
        this.films = films;
    }
}
