package org.djr.securus.camera.rest;

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
import java.util.Map;

/**
 * Created by djr4488 on 12/1/16.
 */
public class CameraAuthorizationFilter implements Filter {
    @Inject
    private Logger log;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.debug("doFilter() entered auth");
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        String authorization = httpRequest.getHeader("authorization");
        if (null != authorization && !"".equals(authorization.trim())) {
            try {
                Map<String, String> authMap = CameraAuthenticationUtils.getUserNameAndPasswordAsMap(authorization);
                HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper((HttpServletRequest) servletRequest);
                wrapper.setAttribute("userName", authMap.get("userName"));
                wrapper.setAttribute("password", authMap.get("password"));
                filterChain.doFilter(wrapper.getRequest(), servletResponse);
            } catch (Exception ex) {
                log.error("authorization error cannot decode auth", ex);
                throw new ServletException("Failed to authorization securus");
            }
        } else {
            log.debug("doFilter() no userName found");
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
