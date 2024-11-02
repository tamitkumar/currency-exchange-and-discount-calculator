package com.currency.exch.discount.calc;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
public class AppTest {

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	void contextLoads() {
		assertNotNull(applicationContext, "The application context should have loaded.");
		assertTrue(applicationContext.containsBean("app"),
				"The application context should contain the App bean.");
	}

	@Test
	void testConfigureMethod() {
		App application = new App();
		SpringApplicationBuilder builder = new SpringApplicationBuilder();
		assertNotNull(application.configure(builder), "The configure method should return a non-null SpringApplicationBuilder instance.");
	}
	
	@Test
    void mainMethodTest() {
        assertDoesNotThrow(() -> App.main(new String[] {}),
                "The main method should execute without throwing exceptions.");
    }
}
