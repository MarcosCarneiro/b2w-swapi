package com.marcos.lima.b2wswapi.controllers;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.marcos.lima.b2wswapi.controllers.exception.PlanetNotFoundException;
import com.marcos.lima.b2wswapi.controllers.exception.UntitledPlanet;
import com.marcos.lima.b2wswapi.domain.Planet;
import com.marcos.lima.b2wswapi.dto.PlanetDTO;
import com.marcos.lima.b2wswapi.services.PlanetService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/planets")
public class PlanetController {

    private Logger logger = LoggerFactory.getLogger(PlanetController.class);

    @Autowired
    private PlanetService planetService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Planet> insert(@RequestBody Planet planet){
        Optional.ofNullable(planet.getName())
                .orElseThrow(UntitledPlanet::new);

        return this.planetService.insert(planet)
                .doOnError(throwable -> this.logger.error(throwable.getLocalizedMessage(), throwable))
                .doOnNext(p -> this.logger.info(String.format("Planet [%s] saved with success!", p.getId())));
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Flux<PlanetDTO> find(@RequestParam(value = "name", required = false) String name){
        return this.planetService.getPlanets(name)
                .doOnError(throwable -> this.logger.error(throwable.getLocalizedMessage(), throwable))
                .doOnComplete(() -> this.logger.info("Planets founded with success!"));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Mono<PlanetDTO> findById(@PathVariable("id") String id){
        return this.planetService.findById(id)
                .switchIfEmpty(Mono.error(new PlanetNotFoundException(id)))
                .doOnError(throwable -> this.logger.error(throwable.getLocalizedMessage(), throwable))
                .doOnNext(p -> this.logger.info(String.format("Planet [%s] founded with success!", p.getId())));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable("id") String id){
        return this.planetService.delete(id)
                .doOnError(throwable -> this.logger.error(throwable.getLocalizedMessage(), throwable))
                .doOnNext(d -> this.logger.info(String.format("Planet [%s] deleted with success!", id)));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public Mono<Planet> update(@RequestBody Planet planet, @PathVariable("id") String id){
        planet.setId(id);
        return this.planetService.update(planet)
                .switchIfEmpty(Mono.error(new PlanetNotFoundException(id)))
                .doOnError(throwable -> this.logger.error(throwable.getLocalizedMessage(), throwable))
                .doOnNext(p -> this.logger.info(String.format("Planet [%s] update with success!", p.getId())));
    }
}
