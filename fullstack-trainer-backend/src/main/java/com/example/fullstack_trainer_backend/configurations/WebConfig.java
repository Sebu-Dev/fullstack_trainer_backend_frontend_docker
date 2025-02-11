package com.example.fullstack_trainer_backend.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*") // Erstmal alles zulassen zum Testen
                .allowedMethods("*")
                .allowedHeaders("*")
                /* .allowCredentials(true) */;
    }

    /*
     * @Override
     * public void addCorsMappings(CorsRegistry registry) {
     * registry.addMapping("/**")
     * .allowedOrigins("https://sebu-dev.github.io/fullstack-trainer/#/",
     * "http://localhost:3000")
     * .allowedMethods("GET", "POST", "PUT", "DELETE")
     * .allowCredentials(true);
     * }
     */

}
