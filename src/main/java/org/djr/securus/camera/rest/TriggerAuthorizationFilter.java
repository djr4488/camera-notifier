package org.djr.securus.camera.rest;

import org.djr.securus.UserController;
import org.djr.securus.entities.User;
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
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by djr4488 on 12/7/16.
 */
public class TriggerAuthorizationFilter implements Filter {
    @Inject
    private Logger log;
    @Inject
    private UserController userController;
    private static final String TRIGGER = "TRIGGER";
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("doFilter() entered");
        HttpServletRequest req = (HttpServletRequest)servletRequest;
        String authorization = req.getHeader("Authorization");
        Map<String, String> authMap = CameraAuthenticationUtils.getUserNameAndPasswordAsMap(authorization);
        log.debug("doFilter() validating userName:{}, password:{}, ip:{}, event:{}", authMap.get("userName"), authMap.get("password"), req.getRemoteAddr(), TRIGGER);
        User user = userController.validateUser(authMap.get("userName"), authMap.get("password"), req.getRemoteAddr(), TRIGGER);
        if (null != user) {
            HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper(req);
            wrapper.setAttribute("user_id", user.getId());
            filterChain.doFilter(req, servletResponse);
        } else {
            log.debug("doFilter() - not validated");
            HttpServletResponse resp = (HttpServletResponse) servletResponse;
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @Override
    public void destroy() {

    }
}
