package com.ciklum.test.github.consumer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.service.Response;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.ciklum.test.github.consumer.exception.GlobalExceptionHandler.NOT_FOUND_USER;
import static com.ciklum.test.github.consumer.exception.GlobalExceptionHandler.UNSUPPORTED_ACCEPT_HEADER;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;
import static org.springframework.http.HttpStatus.NOT_FOUND;

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
        docket.consumes(Set.of(MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE));
        return docket;
    }

    private List<Response> buildErrorResponses() {
        ArrayList<Response> responses = new ArrayList<>();
        responses.add(new ResponseBuilder()
                          .code(String.valueOf(NOT_FOUND.value()))
                          .description(NOT_FOUND_USER).build());
        responses.add(new ResponseBuilder()
                          .code(String.valueOf(NOT_ACCEPTABLE.value()))
                          .description(UNSUPPORTED_ACCEPT_HEADER).build());
        responses.add(new ResponseBuilder()
                          .code(String.valueOf(INTERNAL_SERVER_ERROR.value()))
                          .description(INTERNAL_SERVER_ERROR.getReasonPhrase()).build());

        return responses;
    }
}