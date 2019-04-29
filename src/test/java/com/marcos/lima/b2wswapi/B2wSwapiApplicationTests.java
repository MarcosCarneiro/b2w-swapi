package com.marcos.lima.b2wswapi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.marcos.lima.b2wswapi.controllers.PlanetControllerTest;

@RunWith(SpringRunner.class)
@SpringBootTest
public class B2wSwapiApplicationTests {

	@Test
	public void contextLoads() {
		PlanetControllerTest planetControllerTest = new PlanetControllerTest();
	}

}
