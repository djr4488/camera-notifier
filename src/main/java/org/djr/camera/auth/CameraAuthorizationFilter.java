package org.djr.camera.auth;

import org.slf4j.Logger;
import org.apache.commons.codec.binary.Base64;
import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

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
                String base64Encoded = authorization.substring(6);
                String base64Decoded = new String(Base64.decodeBase64(base64Encoded));
                String[] userNameAndPasswordArray = base64Decoded.split(":");
                HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper((HttpServletRequest) servletRequest);
                wrapper.setAttribute("userName", userNameAndPasswordArray[0]);
                wrapper.setAttribute("password", userNameAndPasswordArray[1]);
                log.debug("doFilter() auth:{}", userNameAndPasswordArray);
                filterChain.doFilter(servletRequest, servletResponse);
            } catch (Exception ex) {
                log.error("authorization error cannot decode auth", ex);
                throw new ServletException("Failed to authorization camera");
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
