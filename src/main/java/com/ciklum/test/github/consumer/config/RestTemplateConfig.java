package com.ciklum.test.github.consumer.config;

import com.ciklum.test.github.consumer.properties.GithubProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Autowired
    private GithubProperties props;

    @Bean
    public RestTemplate githubRestTemplate() {
        RestTemplate githubRestTemplate = new RestTemplateBuilder()
                .rootUri(props.getBaseurl())
                .build();

        return githubRestTemplate;
    }

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }

}
