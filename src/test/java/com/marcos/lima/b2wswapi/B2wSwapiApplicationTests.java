package com.marcos.lima.b2wswapi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.boot.test.context.SpringBootTest;

import com.marcos.lima.b2wswapi.controllers.PlanetControllerTest;
import com.marcos.lima.b2wswapi.repository.PlanetRepositoryTest;

@RunWith(Suite.class)
@SpringBootTest
@Suite.SuiteClasses({ PlanetControllerTest.class, PlanetRepositoryTest.class })
public class B2wSwapiApplicationTests {

	@Test
	public void contextLoads() {

	}

}
