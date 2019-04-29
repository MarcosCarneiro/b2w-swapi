package com.marcos.lima.b2wswapi.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.marcos.lima.b2wswapi.domain.Planet;
import com.marcos.lima.b2wswapi.domain.swapi.SwapiResult;
import com.marcos.lima.b2wswapi.dto.PlanetDTO;
import com.marcos.lima.b2wswapi.repository.PlanetRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PlanetService {

    private static final String SWAPI_URL = "https://swapi.co";
    private static final String URI_ENDPOINT = "/api/planets/?search=";

    private final WebClient webClient = WebClient.create(SWAPI_URL);

    @Autowired
    private PlanetRepository planetRepository;

    public Mono<Planet> insert(Planet planet){
        return this.planetRepository.save(planet);
    }

    public Flux<PlanetDTO> getPlanets(String name){
        return Optional.ofNullable(name)
                .map(this::findByName)
                .orElse(this.findAll());
    }

    private Flux<PlanetDTO> findAll(){
        return this.planetRepository.findAll()
                .flatMap(this::combinePlanetsAndFilms);
    }

    private Flux<PlanetDTO> findByName(String name){
        return this.planetRepository.findByNameLike(name)
                .flatMap(this::combinePlanetsAndFilms);
    }

    public Mono<PlanetDTO> findById(String id){
        return this.planetRepository.findById(id).flux()
                .flatMap(this::combinePlanetsAndFilms)
                .next();
    }

    public Mono<Void> delete(String id){
        return this.planetRepository.deleteById(id);
    }

    public Mono<Planet> update(Planet planet){
        return this.planetRepository.findById(planet.getId())
                .flatMap(p -> this.planetRepository.save(planet))
                .switchIfEmpty(Mono.empty());
    }

    private Mono<Integer> getFilmsThatAppear(String planetName){
         return this.webClient.get().uri(URI_ENDPOINT + planetName)
                 .retrieve()
                 .bodyToMono(SwapiResult.class)
                 .filter(swapiResult -> swapiResult.getCount() > 0)
                 .map(swapiResult ->
                         swapiResult.getResults()
                                 .stream().findFirst()
                                 .map(planetResult -> planetResult.getFilms().size())
                 ).flatMap(amountFilms -> Mono.just(amountFilms.orElse(0)))
                 .defaultIfEmpty(0)
                 .onErrorReturn(0);
    }

    private Flux<PlanetDTO> combinePlanetsAndFilms(Planet planet){
        return Flux.combineLatest(Mono.just(planet),
                this.getFilmsThatAppear(planet.getName()),
                (p, a) -> new PlanetDTO(p, a));
    }
}
