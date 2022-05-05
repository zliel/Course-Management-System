package com.personal.springbootpractice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SpringFoxConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.personal.springbootpractice").and(RequestHandlerSelectors.withClassAnnotation(RestController.class)))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfo());
    }

    public ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                .contact(new Contact("Zackary Liel", "https://github.com/zliel", "z_liel@u.pacific.edu"))
                .title("Course Management System")
                .description("This is a system to keep track of courses in a user's school.")
                .build();
    }
}
