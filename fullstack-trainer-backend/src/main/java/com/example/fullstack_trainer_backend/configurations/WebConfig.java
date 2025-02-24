package com.example.fullstack_trainer_backend.configurations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        logger.info("CORS-Konfiguration wird geladen");
        registry.addMapping("/**")
                .allowedOrigins(
                        "https://sebu-dev.github.io", // GitHub Pages
                        "http://localhost:3000", // Lokale Entwicklung
                        "https://sebu-dev.de", // Server selbst (über Domain)
                        "http://217.154.77.26:8080" // Server direkt (über IP, optional)
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*");
    }
}