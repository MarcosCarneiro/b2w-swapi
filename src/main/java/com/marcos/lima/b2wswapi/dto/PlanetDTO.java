package com.marcos.lima.b2wswapi.dto;

import java.util.Objects;

import com.marcos.lima.b2wswapi.domain.Planet;

public class PlanetDTO {

    private String id;
    private String name;
    private String climate;
    private String terrain;
    private Integer filmsThatAppear;

    public PlanetDTO(){}

    public PlanetDTO(Planet planet, Integer filmsThatAppear){
        this.id = planet.getId();
        this.name = planet.getName();
        this.climate = planet.getClimate();
        this.terrain = planet.getTerrain();
        this.filmsThatAppear = filmsThatAppear;

    }

    public PlanetDTO(String id, String name, String climate, String terrain, Integer filmsThatAppear) {
        this.id = id;
        this.name = name;
        this.climate = climate;
        this.terrain = terrain;
        this.filmsThatAppear = filmsThatAppear;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClimate() {
        return this.climate;
    }

    public void setClimate(String climate) {
        this.climate = climate;
    }

    public String getTerrain() {
        return this.terrain;
    }

    public void setTerrain(String terrain) {
        this.terrain = terrain;
    }

    public Integer getFilmsThatAppear() {
        return this.filmsThatAppear;
    }

    public void setFilmsThatAppear(Integer filmsThatAppear) {
        this.filmsThatAppear = filmsThatAppear;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        PlanetDTO planetDTO = (PlanetDTO) o;
        return Objects.equals(this.id, planetDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}
