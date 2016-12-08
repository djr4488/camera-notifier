package org.djr.camera.auth;

import org.slf4j.Logger;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

/**
 * Created by djr4488 on 12/7/16.
 */
public class SensorAuthorizationFilter implements Filter {
    @Inject
    private Logger log;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    //TODO validate user
    //TODO if already validated should have a session and validate token
    //TODO if not validated should create a session and validate against User entity
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("doFilter() entered");
        //for now hard coding userName and cameraName for testing plumbing
        HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper((HttpServletRequest) servletRequest);
        wrapper.setAttribute("userName", "testUser");
        wrapper.setAttribute("password", "cameraName");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
