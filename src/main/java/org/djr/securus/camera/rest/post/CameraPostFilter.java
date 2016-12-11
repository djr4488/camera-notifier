package org.djr.securus.camera.rest.post;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

/**
 * @author jonathanfisher
 */
public class CameraPostFilter implements Filter {
    @Inject
    private Logger log;

    /**
     * {@inheritDoc}
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        log.debug("doFilter() postFilter entered");
        HttpServletRequestWrapper wrapper = new HttpServletRequestWrapper((HttpServletRequest)request);
        File tempFile = new File(UUID.randomUUID().toString()+".avi");
        tempFile.createNewFile();
        FileOutputStream out = new FileOutputStream(tempFile);
        IOUtils.copy(request.getInputStream(), out);
        wrapper.setAttribute("file", tempFile);
        chain.doFilter(request, response);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() {
    }
}