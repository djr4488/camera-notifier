package org.djr.securus.user.rest;

import org.djr.securus.user.UserAuthorizationService;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class UserAuthorizationFilter implements Filter {
    @Inject
    private Logger log;
    @Inject
    private UserAuthorizationService userAuthorizationService;

    @Override
    public void init(FilterConfig filterConfig)
    throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
    throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpSession httpSession = httpServletRequest.getSession(false);
        if (null != httpSession && isValidToken(httpSession)) {
            log.debug("doFilter() validated token");
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            log.debug("doFilter() - not validated");
            HttpServletResponse resp = (HttpServletResponse) servletResponse;
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @Override
    public void destroy() {

    }

    private boolean isValidToken(HttpSession session) {
        boolean isValidToken = false;
        String token = (String)session.getAttribute("token");
        if (null != token && token.equals(userAuthorizationService.getToken(token).getToken())) {
            isValidToken = true;
        }
        return isValidToken;
    }
}
