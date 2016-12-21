package org.djr.securus.user.rest.add;

import junit.framework.TestCase;
import org.djr.securus.CommonTestEntityUtils;
import org.djr.securus.UserController;
import org.djr.securus.camera.CameraEventService;
import org.djr.securus.cdi.logs.LoggerProducer;
import org.djr.securus.entities.User;
import org.djr.securus.messaging.email.EmailService;
import org.djr.securus.user.UserLookupService;
import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by djr4488 on 12/21/16.
 */
@RunWith(CdiRunner.class)
@AdditionalClasses({LoggerProducer.class, Event.class, UserController.class})
public class AddUserInitiatorTest extends TestCase {
    @Produces
    @Mock
    private UserLookupService userLookupService;
    @Produces
    @Mock
    private CameraEventService cameraEventService;
    @Produces
    @Mock
    private EmailService emailService;
    @Inject
    private AddUserInitiator addUserInitiator;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddUser() {
        AddUserRequest request = CommonTestEntityUtils.getAddUserRequest();
        HttpServletRequest httpReq = mock(HttpServletRequest.class);
        when(userLookupService.lookupUserByUserName("user")).thenReturn(null);
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        addUserInitiator.doAddUser(httpReq, request);
        verify(userLookupService, times(1)).persistNewUser(userArgumentCaptor.capture());
        User user = userArgumentCaptor.getValue();
        assertNotNull(user);
        assertEquals("user", user.getUserName());
        assertEquals("test@test.com", user.getEmailAddress());
    }
}
