package com.ciklum.test.github.consumer.controller.filters;


import static com.ciklum.test.github.consumer.exception.GlobalExceptionHandler.UNSUPPORTED_ACCEPT_HEADER;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class HeaderValidationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String headerValue = request.getHeader("Accept");

        if (headerValue.equals("application/xml")) {
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"status\": 406, \"Message\": \"" + UNSUPPORTED_ACCEPT_HEADER + "\"}");
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

}
