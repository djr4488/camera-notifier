package org.djr.securus.camera.rest;

import junit.framework.TestCase;
import org.apache.commons.codec.binary.Base64;
import org.djr.securus.CommonTestEntityUtils;
import org.djr.securus.UserController;
import org.djr.securus.cdi.logs.LoggerProducer;
import org.djr.securus.entities.User;
import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.slf4j.Logger;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by djr4488 on 1/4/17.
 */
@RunWith(CdiRunner.class)
@AdditionalClasses({LoggerProducer.class})
public class TriggerAuthorizationFilterTest extends TestCase {
    @Mock
    @Produces
    private UserController userController;
    @Inject
    private TriggerAuthorizationFilter triggerAuthorizationFilter;
    @Inject
    private Logger log;

    @Test
    public void testDoFilter() {
        HttpServletRequest httpReq = mock(HttpServletRequest.class);
        HttpServletResponse httpResp = mock(HttpServletResponse.class);
        FilterChain fltChn = mock(FilterChain.class);
        User user = CommonTestEntityUtils.getUser();
        String base64Auth = "BASIC " + Base64.encodeBase64String("test:test".getBytes());
        when(httpReq.getHeader("Authorization")).thenReturn(base64Auth);
        when(httpReq.getRemoteAddr()).thenReturn("0.0.0.0");
        when(userController.validateUser("test", "test", "0.0.0.0", "TRIGGER")).thenReturn(user);
        try {
            triggerAuthorizationFilter.doFilter(httpReq, httpResp, fltChn);
            verify(userController, times(1)).validateUser("test", "test", "0.0.0.0", "TRIGGER");
            verify(fltChn, times(1)).doFilter(httpReq, httpResp);
        } catch (Exception ex) {
            log.error("error:", ex);
            fail("failed test see stack trace");
        }
    }

    @Test
    public void testDoFilterUnauthorized() {
        HttpServletRequest httpReq = mock(HttpServletRequest.class);
        HttpServletResponse httpResp = mock(HttpServletResponse.class);
        FilterChain fltChn = mock(FilterChain.class);
        String base64Auth = "BASIC " + Base64.encodeBase64String("test:test".getBytes());
        when(httpReq.getHeader("Authorization")).thenReturn(base64Auth);
        when(httpReq.getRemoteAddr()).thenReturn("0.0.0.0");
        when(userController.validateUser("test", "test", "0.0.0.0", "TRIGGER")).thenReturn(null);
        try {
            triggerAuthorizationFilter.doFilter(httpReq, httpResp, fltChn);
            verify(userController, times(1)).validateUser("test", "test", "0.0.0.0", "TRIGGER");
            verify(fltChn, never()).doFilter(httpReq, httpResp);
            verify(httpResp, times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (Exception ex) {
            log.error("error:", ex);
            fail("failed test see stack trace");
        }
    }
}
