package com.currency.exch.discount.calc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

	@Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Currency Exchange and Discount Calculation API")
                        .version("1.0")
                        .description("API for calculating currency exchange rates and applying discounts.")
                        .contact(new Contact().name("Amit Kumar Tiwari").email("tamitkumar16@gmail.com")));
    }
}
