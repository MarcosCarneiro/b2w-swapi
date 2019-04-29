package com.marcos.lima.b2wswapi.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.marcos.lima.b2wswapi.domain.Planet;
import reactor.core.publisher.Flux;

public interface PlanetRepository extends ReactiveCrudRepository<Planet, String> {
    Flux<Planet> findByNameLike(String name);
}
