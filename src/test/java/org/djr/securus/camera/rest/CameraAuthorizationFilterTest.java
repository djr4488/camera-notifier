package org.djr.securus.camera.rest;

import junit.framework.TestCase;
import org.apache.commons.codec.binary.Base64;
import org.djr.securus.cdi.logs.LoggerProducer;
import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by djr4488 on 1/4/17.
 */
@RunWith(CdiRunner.class)
@AdditionalClasses({LoggerProducer.class})
public class CameraAuthorizationFilterTest extends TestCase {
    @Inject
    private CameraAuthorizationFilter cameraAuthorizationFilter;
    @Inject
    private Logger log;

    @Test
    public void testDoFilter() {
        HttpServletRequest httpReq = mock(HttpServletRequest.class);
        HttpServletResponse httpResp = mock(HttpServletResponse.class);
        FilterChain fltChn = mock(FilterChain.class);
        String base64Auth = "BASIC " + Base64.encodeBase64String("test:test".getBytes());
        when(httpReq.getHeader("authorization")).thenReturn(base64Auth);
        try {
            cameraAuthorizationFilter.doFilter(httpReq, httpResp, fltChn);
            verify(fltChn, times(1)).doFilter(httpReq, httpResp);

        } catch (Exception ex) {
            log.error("error: ", ex);
            fail("failed test");
        }
    }

    @Test
    public void testDoFilterAuthorizationNull() {
        HttpServletRequest httpReq = mock(HttpServletRequest.class);
        HttpServletResponse httpResp = mock(HttpServletResponse.class);
        FilterChain fltChn = mock(FilterChain.class);
        String base64Auth = null;
        when(httpReq.getHeader("authorization")).thenReturn(base64Auth);
        ArgumentCaptor<HttpServletRequest> httpServletRequestArgumentCaptor = ArgumentCaptor.forClass(HttpServletRequest.class);
        try {
            cameraAuthorizationFilter.doFilter(httpReq, httpResp, fltChn);
            verify(fltChn, times(1)).doFilter(httpServletRequestArgumentCaptor.capture(), any(HttpServletResponse.class));
            HttpServletRequest captured = httpServletRequestArgumentCaptor.getValue();
            assertNull(captured.getAttribute("userName"));
        } catch (Exception ex) {
            log.error("error: ", ex);
            fail("failed test");
        }
    }

    @Test
    public void testDoFilterAuthorizationEmptyString() {
        HttpServletRequest httpReq = mock(HttpServletRequest.class);
        HttpServletResponse httpResp = mock(HttpServletResponse.class);
        FilterChain fltChn = mock(FilterChain.class);
        String base64Auth = "";
        when(httpReq.getHeader("authorization")).thenReturn(base64Auth);
        ArgumentCaptor<HttpServletRequest> httpServletRequestArgumentCaptor = ArgumentCaptor.forClass(HttpServletRequest.class);
        try {
            cameraAuthorizationFilter.doFilter(httpReq, httpResp, fltChn);
            verify(fltChn, times(1)).doFilter(httpServletRequestArgumentCaptor.capture(), any(HttpServletResponse.class));
            HttpServletRequest captured = httpServletRequestArgumentCaptor.getValue();
            assertNull(captured.getAttribute("userName"));
        } catch (Exception ex) {
            log.error("error: ", ex);
            fail("failed test");
        }
    }

    @Test(expected = ServletException.class)
    public void testDoFilterException()
    throws IOException, ServletException {
        HttpServletRequest httpReq = mock(HttpServletRequest.class);
        HttpServletResponse httpResp = mock(HttpServletResponse.class);
        FilterChain fltChn = mock(FilterChain.class);
        String base64Auth = "BASIC " + Base64.encodeBase64String("test:test".getBytes());
        when(httpReq.getHeader("authorization")).thenReturn(base64Auth);
        try {
            doThrow(new ServletException("test")).when(fltChn).doFilter(any(HttpServletRequest.class), any(HttpServletResponse.class));
            cameraAuthorizationFilter.doFilter(httpReq, httpResp, fltChn);
            verify(fltChn, times(1)).doFilter(httpReq, httpResp);
        } catch (IOException ex) {
            log.error("error: ", ex);
            fail("expected servlet exception");
        }
    }
}
