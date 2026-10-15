package com.oficioya.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI oficioYaOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("OficioYa API")
                        .description("API REST del backend de OficioYa — plataforma de conexión entre contratantes y trabajadores de oficios.")
                        .version("0.0.1-SNAPSHOT")
                        .contact(new Contact()
                                .name("Equipo — DOSW")
                                .email("equipo28@eci.edu.co")));
    }
}
