package com.ciklum.test.github.consumer.controller.filters;


import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.ciklum.test.github.consumer.exception.GlobalExceptionHandler.INVALID_ACCEPT_HEADER_FORMAT;
import static com.ciklum.test.github.consumer.exception.GlobalExceptionHandler.UNSUPPORTED_ACCEPT_HEADER;

@Component
public class HeaderValidationFilter implements Filter {


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String headerValue = request.getHeader("Accept");

        if (headerValue.equals("application/xml")) {
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.setStatus(INVALID_ACCEPT_HEADER_FORMAT);
            response.setContentType("application/json");
            response.getWriter().write("{\"status\": 406, \"Message\": \"" + UNSUPPORTED_ACCEPT_HEADER + "\"}");
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

}
