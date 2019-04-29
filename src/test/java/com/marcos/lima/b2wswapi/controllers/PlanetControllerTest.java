package com.marcos.lima.b2wswapi.controllers;

import static org.mockito.BDDMockito.given;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import com.marcos.lima.b2wswapi.domain.Planet;
import com.marcos.lima.b2wswapi.dto.PlanetDTO;
import com.marcos.lima.b2wswapi.services.PlanetService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RunWith(SpringRunner.class)
@WebFluxTest(controllers = PlanetController.class)
public class PlanetControllerTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private PlanetService planetService;

    @Test
    public void testGetPlanetByIdShoudBeOk(){
        PlanetDTO planet = new PlanetDTO("123abc", "Dagobah", "climate", "terrain", 1);

        given(this.planetService.findById("123abc")).willReturn(Mono.just(planet));

        this.client.get().uri("/planets/123abc").exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo("123abc")
                .jsonPath("$.name").isEqualTo("Dagobah")
                .jsonPath("$.climate").isEqualTo("climate")
                .jsonPath("$.terrain").isEqualTo("terrain");
    }

    @Test
    public void testGetInvalidPlanetShouldReturnNotFound(){
        given(this.planetService.findById("123abc")).willReturn(Mono.empty());

        this.client.get().uri("/planets/123abc").exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void testPostPlanetShouldReturnCreate(){
        Planet planet = new Planet("123abc", "Dagobah", "climate", "terrain");

        given(this.planetService.insert(BDDMockito.any(Planet.class))).willReturn(Mono.just(planet));

        this.client.post().uri("/planets")
                .body(BodyInserters.fromObject(planet)).exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo("123abc")
                .jsonPath("$.name").isEqualTo("Dagobah")
                .jsonPath("$.climate").isEqualTo("climate")
                .jsonPath("$.terrain").isEqualTo("terrain");
    }

    @Test
    public void testPostInvalidPlanetShouldReturnBadRequest() {
        Planet planet = new Planet();
        planet.setClimate("climate");

        this.client.post().uri("/planets")
                .body(BodyInserters.fromObject(planet))
                .exchange().expectStatus().isBadRequest();
    }

    @Test
    public void testDeletePlanetShouldReturnNoContent(){
        given(this.planetService.delete("123abc")).willReturn(Mono.empty());

        this.client.delete().uri("/planets/123abc").exchange()
                .expectStatus().isNoContent();
    }

    @Test
    public void testGetAllPlanetsShouldBeOk(){
        PlanetDTO p1 = new PlanetDTO("123abc", "Dagobah", "climate", "terrain", 2);
        PlanetDTO p2 = new PlanetDTO("abc123", "Naboo", "climate", "terrain", 3);

        given(this.planetService.getPlanets("")).willReturn(Flux.just(p1, p2));

        this.client.get().uri("/planets?name=")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_STREAM_JSON)
                .expectBodyList(PlanetDTO.class);
    }
}
