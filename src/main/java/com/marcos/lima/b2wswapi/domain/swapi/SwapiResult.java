package com.marcos.lima.b2wswapi.domain.swapi;

import java.io.Serializable;
import java.util.List;

public class SwapiResult implements Serializable {
    private static final long serialVersionUID = 1L;

    private int count;
    private List<PlanetResult> results;

    public SwapiResult(){}

    public SwapiResult(int count, List<PlanetResult> results) {
        this.count = count;
        this.results = results;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<PlanetResult> getResults() {
        return this.results;
    }

    public void setResults(List<PlanetResult> results) {
        this.results = results;
    }
}
