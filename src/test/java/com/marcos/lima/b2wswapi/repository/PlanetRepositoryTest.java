package com.marcos.lima.b2wswapi.repository;

import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.test.context.junit4.SpringRunner;

import com.marcos.lima.b2wswapi.domain.Planet;
import com.mongodb.reactivestreams.client.MongoCollection;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PlanetRepositoryTest {

    @Autowired
    private PlanetRepository planetRepository;

    @Autowired
    private ReactiveMongoOperations operations;

    @Before
    public void setUp(){
        final Mono<MongoCollection<Document>> recreateCollection = this.operations.collectionExists(Planet.class)
                .flatMap(exists -> exists ? this.operations.dropCollection(Planet.class) : Mono.just(exists))
                .then(this.operations.createCollection(Planet.class, CollectionOptions.empty()));

        StepVerifier.create(recreateCollection).expectNextCount(1).verifyComplete();

        final Flux<Planet> planets = this.operations.insertAll(
                Flux.just(
                        new Planet("123abc", "Dagobah", "climate", "terrain"),
                        new Planet("456def", "Naboo", "climate", "terrain"),
                        new Planet("789ghi", "Tatooine", "climate", "terrain")
                ).collectList());

        StepVerifier.create(planets).expectNextCount(3).verifyComplete();
    }

    @Test
    public void findByIdShouldReturnOnePlanet(){
        Mono<Planet> planet = this.planetRepository.findById("123abc");
        StepVerifier.create(planet)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void findByInvalidIdShouldReturnEmpty(){
        Mono<Planet> planet = this.planetRepository.findById("987xyz");
        StepVerifier.create(planet)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    public void deleteAndFindPlanetShouldOk(){
        Flux<Planet> planet = this.planetRepository.deleteById("123abc")
                .thenMany(this.planetRepository.findById("123abc"));
        StepVerifier.create(planet)
                .expectSubscription()
                .expectNextCount(0)
                .expectComplete();
    }

    @Test
    public void savePlanetShouldOk(){
        Mono<Planet> planet = this.planetRepository.save(new Planet("321cba", "Hoth", "climate", "terrain"));
        StepVerifier.create(planet)
                .expectNextCount(1)
                .expectComplete();
    }
}
