package com.bank.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableWebMvc
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("http://localhost:8080").allowedMethods("PUT", "DELETE",
                "GET", "POST", "HEAD", "OPTIONS").allowedHeaders("Accept", "Content-Type", "Content-Length", "Accept-Encoding", "X-CSRF-Token", "Origin", "Content_Type", "Access-Control-Allow-Headers", "Authorization", "X-Requested-With");
    }
}

