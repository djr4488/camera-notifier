package org.djr.securus.user.rest.password;

import junit.framework.TestCase;
import org.djr.securus.CommonTestEntityUtils;
import org.djr.securus.UserController;
import org.djr.securus.cdi.logs.LoggerProducer;
import org.djr.securus.user.UserException;
import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by djr4488 on 12/24/16.
 */
@RunWith(CdiRunner.class)
@AdditionalClasses({LoggerProducer.class})
public class PasswordInitiatorTest extends TestCase {
    @Mock
    @Produces
    private UserController userController;
    @Inject
    private PasswordInitiator passwordInitiator;

    @Test
    public void doChangePasswordSuccess() {
        HttpServletRequest httpReq = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        ChangePasswordRequest req = CommonTestEntityUtils.getChangePasswordRequest();
        when(httpReq.getSession(false)).thenReturn(session);
        when(session.getAttribute("userId")).thenReturn(1L);
        when(httpReq.getRemoteAddr()).thenReturn("0.0.0.0");
        when(userController.changePassword(1L, "old", "new", "new", "0.0.0.0"))
                 .thenReturn(true);
        Response resp = passwordInitiator.doChangePassword(httpReq, req);
        assertNotNull(resp);
        assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
    }

    @Test(expected = UserException.class)
    public void doChangePasswordFails() {
        HttpServletRequest httpReq = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        ChangePasswordRequest req = CommonTestEntityUtils.getChangePasswordRequest();
        when(httpReq.getSession(false)).thenReturn(session);
        when(session.getAttribute("userId")).thenReturn(1L);
        when(httpReq.getRemoteAddr()).thenReturn("0.0.0.0");
        when(userController.changePassword(1L, "old", "new", "new", "0.0.0.0"))
                .thenReturn(false);
        Response resp = passwordInitiator.doChangePassword(httpReq, req);
        assertNull(resp);
    }
}
