package com.lrrsmeiksts.EmployeeManagement_API.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Bean
    public OpenAPI customOpenApi(){
        return new OpenAPI().info(apiInfo());
    }

    private Info apiInfo(){
        return new Info()
                .title("Quizz API")
                .description("API for guessing quizzes")
                .version("1.0");
    }
}