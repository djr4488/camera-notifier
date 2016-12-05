package org.djr.camera.request;

import org.slf4j.Logger;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Enumeration;

import static org.apache.commons.io.IOUtils.toByteArray;

/**
 * Created by djr4488 on 12/4/16.
 */
public class RequestFilter implements Filter {
    @Inject
    private Logger log;

    @Override
    public void init(FilterConfig filterConfig)
    throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
    throws IOException, ServletException {
        printEnumerationStrings("attributeName", request.getAttributeNames());
        printEnumerationStrings("parameterName", request.getParameterNames());
        printEnumerationStrings("headerName", ((HttpServletRequest)request).getHeaderNames());
        printHeaderValues(((HttpServletRequest)request));
        log.debug("doFilter() uri:{}", ((HttpServletRequest)request).getRequestURI());
        log.debug("doFilter() url:{}", ((HttpServletRequest)request).getRequestURL());
        byte[] buf = toByteArray(request.getInputStream());
        log.debug("doFilter() - buf length:{}", buf.length);
        String originalRequest =
                new String(buf, request.getCharacterEncoding() != null ? request.getCharacterEncoding() : "UTF-8");
        log.debug("doFilter() - originalRequest:{}", originalRequest);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

    private void printEnumerationStrings(String key, Enumeration<String> enumStringsInRequest) {
        while (enumStringsInRequest.hasMoreElements()) {
            log.debug("doFilter {}:{}", key, enumStringsInRequest.nextElement());
        }
    }

    private void printHeaderValues(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            log.debug("printHeaderValues() {}:{}", headerName, request.getHeader(headerName));
        }
    }
}