package org.djr.securus.user.rest.login;

import junit.framework.TestCase;
import org.djr.securus.CommonTestEntityUtils;
import org.djr.securus.TokenService;
import org.djr.securus.UserController;
import org.djr.securus.cdi.logs.LoggerProducer;
import org.djr.securus.entities.Token;
import org.djr.securus.entities.User;
import org.djr.securus.messaging.email.EmailService;
import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by djr4488 on 12/21/16.
 */
@RunWith(CdiRunner.class)
@AdditionalClasses({LoggerProducer.class, Event.class})
public class LoginInitiatorTest extends TestCase {
    @Produces
    @Mock
    private UserController userContoller;
    @Produces
    @Mock
    private TokenService tokenService;
    @Produces
    @Mock
    private EmailService emailService;
    @Inject
    private LoginInitiator loginInitiator;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDoLoginNoSessionHasUser() {
        HttpServletRequest httpReq = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        LoginRequest request = CommonTestEntityUtils.getLoginRequest();
        User user = CommonTestEntityUtils.getUser();
        Token token = CommonTestEntityUtils.getToken(user);
        when(httpReq.getSession(false)).thenReturn(null);
        when(httpReq.getSession()).thenReturn(session);
        when(httpReq.getRemoteAddr()).thenReturn("0.0.0.0");
        when(userContoller.validateUser(request.getUserName(), request.getPassword(), "0.0.0.0", "LOGIN"))
            .thenReturn(user);
        when(tokenService.generateToken(user)).thenReturn(token);
        Response response = loginInitiator.doLogin(httpReq, request);
        verify(session, times(1)).setAttribute("token", token.getToken());
        verify(session, times(1)).setAttribute("userId", user.getId());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        LoginResponse loginResponse = (LoginResponse)response.getEntity();
        assertNotNull(loginResponse);
        assertEquals("/api/user/management", loginResponse.getForwardUrl());
        assertNull(loginResponse.getMsg());
        assertNull(loginResponse.getMsgBold());
    }

    @Test
    public void testDoLoginNoUser() {
        HttpServletRequest httpReq = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        LoginRequest request = CommonTestEntityUtils.getLoginRequest();
        User user = null;
        Token token = CommonTestEntityUtils.getToken(user);
        when(httpReq.getSession(false)).thenReturn(null);
        when(httpReq.getRemoteAddr()).thenReturn("0.0.0.0");
        when(userContoller.validateUser(request.getUserName(), request.getPassword(), "0.0.0.0", "LOGIN"))
                .thenReturn(user);
        Response response = loginInitiator.doLogin(httpReq, request);
        verify(session, never()).setAttribute("token", token.getToken());
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
    }

    @Test
    public void testDoLoginValidSession() {
        HttpServletRequest httpReq = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        LoginRequest request = CommonTestEntityUtils.getLoginRequest();
        User user = CommonTestEntityUtils.getUser();
        Token token = CommonTestEntityUtils.getToken(user);
        when(httpReq.getSession(false)).thenReturn(session);
        when(httpReq.getRemoteAddr()).thenReturn("0.0.0.0");
        when(session.getAttribute("token")).thenReturn(token);
        when(userContoller.validateUser(request.getUserName(), request.getPassword(), "0.0.0.0", "LOGIN"))
                .thenReturn(user);
        when(tokenService.generateToken(user)).thenReturn(token);
        Response response = loginInitiator.doLogin(httpReq, request);
        verify(session, never()).setAttribute("token", token.getToken());
        verify(session, never()).setAttribute("userId", user.getId());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        LoginResponse loginResponse = (LoginResponse)response.getEntity();
        assertNotNull(loginResponse);
        assertEquals("/api/user/management", loginResponse.getForwardUrl());
        assertNull(loginResponse.getMsg());
        assertNull(loginResponse.getMsgBold());
    }
}
