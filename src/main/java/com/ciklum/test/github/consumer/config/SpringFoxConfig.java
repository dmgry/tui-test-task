package com.ciklum.test.github.consumer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.service.Response;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

import static com.ciklum.test.github.consumer.exception.GlobalExceptionHandler.NOT_FOUND_USER;
import static com.ciklum.test.github.consumer.exception.GlobalExceptionHandler.UNSUPPORTED_ACCEPT_HEADER;

@Configuration
public class SpringFoxConfig {

    @Bean
    public Docket api() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ciklum.test.github.consumer.controller"))
                .paths(PathSelectors.any())
                .build();
        docket.useDefaultResponseMessages(false)
                .globalResponses(HttpMethod.GET, buildErrorResponses());

        return docket;
    }

    private List<Response> buildErrorResponses() {
        ArrayList<Response> responses = new ArrayList<>();
        responses.add(new ResponseBuilder().code("404").description(NOT_FOUND_USER).build());
        responses.add(new ResponseBuilder().code("406").description(UNSUPPORTED_ACCEPT_HEADER).build());
        responses.add(new ResponseBuilder().code("500").description("Internal server error").build());

        return responses;
    }
}