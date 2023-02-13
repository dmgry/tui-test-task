package com.ciklum.test.github.consumer.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.WebApplicationInitializer;

import com.ciklum.test.github.consumer.controller.filters.HeaderValidationFilter;


@Configuration
public class HeaderValidationConfig implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext
            .addFilter("headerValidationFilter", new HeaderValidationFilter())
            .addMappingForUrlPatterns(null, false, "/*");
    }
}
