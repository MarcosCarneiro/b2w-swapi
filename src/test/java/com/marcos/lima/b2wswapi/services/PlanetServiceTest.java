package com.marcos.lima.b2wswapi.services;

import static org.mockito.BDDMockito.given;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.marcos.lima.b2wswapi.domain.Planet;
import com.marcos.lima.b2wswapi.dto.PlanetDTO;
import com.marcos.lima.b2wswapi.repository.PlanetRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PlanetServiceTest {

    @MockBean
    private PlanetRepository planetRepository;

    @Autowired
    private PlanetService planetService;

    @Test
    public void savePlanetShouldBeOk(){
        Planet planet = new Planet("123abc", "Dagobah", "climate", "terrain");

        given(this.planetRepository.save(BDDMockito.any(Planet.class))).willReturn(Mono.just(planet));

        Mono<Planet> planetMono = this.planetService.insert(planet);
        StepVerifier.create(planetMono)
                .expectNextMatches(savedPlanet -> savedPlanet.getName().equals(("Dagobah")))
                .verifyComplete();
    }

    @Test
    public void findPlanetByIdShouldBeOk(){
        Planet planet = new Planet("123abc", "Dagobah", "climate", "terrain");

        given(this.planetRepository.findById("123abc")).willReturn(Mono.just(planet));

        Mono<PlanetDTO> planetDTO = this.planetService.findById("123abc");
        StepVerifier.create(planetDTO)
                .expectNextMatches(planetDTO1 -> planetDTO1.getId().equals("123abc") && planetDTO1.getFilmsThatAppear() > 0)
                .verifyComplete();
    }

    @Test
    public void findInvalidPlanetByIdShoudReturnEmpty(){

        given(this.planetRepository.findById("123xaybzc")).willReturn(Mono.empty());

        Mono<PlanetDTO> planetDTO = this.planetService.findById("123xaybzc");
        StepVerifier.create(planetDTO)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    public void findPlanetByNameShouldBeOk(){
        Planet planet = new Planet("123abc", "Dagobah", "climate", "terrain");

        given(this.planetRepository.findByNameLike("Dagobah")).willReturn(Flux.just(planet));

        Flux<PlanetDTO> planetDTO = this.planetService.getPlanets("Dagobah");
        StepVerifier.create(planetDTO)
                .expectNextMatches(planetDTO1 -> planetDTO1.getId().equals("123abc") && planetDTO1.getFilmsThatAppear() > 0)
                .verifyComplete();
    }

    @Test
    public void findAllPlanetsShouldBeOk(){
        Flux<Planet> planets = Flux.just(
                new Planet("123abc", "Dagobah", "climate", "terrain"),
                new Planet("456def", "Naboo", "climate", "terrain"),
                new Planet("789ghi", "Tatooine", "climate", "terrain")
        );

        given(this.planetRepository.findByNameLike("")).willReturn(planets);

        Flux<PlanetDTO> planetDTO = this.planetService.getPlanets("");
        StepVerifier.create(planetDTO)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    public void updatePlanetShouldBeOk(){
        Planet planet = new Planet("123abc", "Dagobah", "climate", "terrain");
        Planet editedPlanet = new Planet("123abc", "Dagobah", "climate edited", "terrain edited");

        given(this.planetRepository.findById("123abc")).willReturn(Mono.just(planet));
        given(this.planetRepository.save(BDDMockito.any(Planet.class))).willReturn(Mono.just(editedPlanet));

        Mono<Planet> planetMono = this.planetService.update(editedPlanet);
        StepVerifier.create(planetMono)
                .expectNextMatches(planet1 -> planet1.getClimate().equals("climate edited"))
                .verifyComplete();
    }

    @Test
    public void updateInvalidPlanetShouldReturnEmpty(){
        Planet editedPlanet = new Planet("123axbycz", "Dagobah", "climate edited", "terrain edited");

        given(this.planetRepository.findById("123axbycz")).willReturn(Mono.empty());
        given(this.planetRepository.save(BDDMockito.any(Planet.class))).willReturn(Mono.just(editedPlanet));

        Mono<Planet> planetMono = this.planetService.update(editedPlanet);
        StepVerifier.create(planetMono)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    public void deletePlanetShouldBeOk(){
        given(this.planetRepository.deleteById("123abc")).willReturn(Mono.empty());

        Mono<Void> delete = this.planetService.delete("123abc");
        StepVerifier.create(delete)
                .expectNextCount(0)
                .verifyComplete();
    }
}
