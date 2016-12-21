package org.djr.securus;

import junit.framework.TestCase;
import org.djr.securus.camera.CameraEventService;
import org.djr.securus.cdi.logs.LoggerProducer;
import org.djr.securus.entities.Camera;
import org.djr.securus.entities.User;
import org.djr.securus.exceptions.BusinessException;
import org.djr.securus.messaging.email.EmailService;
import org.djr.securus.user.UserLookupService;
import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.io.File;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by djr4488 on 12/20/16.
 */
@RunWith(CdiRunner.class)
@AdditionalClasses({LoggerProducer.class})
public class CameraControllerTest extends TestCase {
    @Mock
    @Produces
    private EmailService emailService;
    @Mock
    @Produces
    private UserLookupService userLookupService;
    @Mock
    @Produces
    private CameraEventService cameraEventService;
    @Inject
    private CameraController cameraController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    //given: camera post event
    //  and: user
    //  and: camera where post event can be processed
    // when: handling the camera post event
    // then: expect the post event to be persisted
    //  and: email service to send email with attachment
    //  and: file to be deleted
    @Test
    public void testCameraPostEventSuccess() {
        File f = mock(File.class);
        CameraPostEvent cameraPostEvent = CommonTestEntityUtils.getCameraPostEvent();
        cameraPostEvent.setFile(f);
        Camera camera = CommonTestEntityUtils.getCamera("name", "zoneName");
        User user = CommonTestEntityUtils.getUser();
        doNothing().when(cameraEventService).persistCameraPostEvent(cameraPostEvent);
        when(userLookupService.lookupUserByUserName("userName")).thenReturn(user);
        when(cameraEventService.lookupCameraByNameAndUser("name", "userName")).thenReturn(camera);
        cameraController.handleCameraPostEvent(cameraPostEvent);
        verify(cameraEventService, times(1)).persistCameraPostEvent(cameraPostEvent);
        verify(emailService, times(1)).sendFileAttachmentEmail(cameraPostEvent, "test@test.com");
        verify(f, times(1)).delete();
    }

    //given: camera post event
    //  and: user
    //  and: camera where post event can be processed
    // when: handling the camera post event
    // then: expect the post event to be persisted
    //  and: email service to not send email
    //  and: file to be deleted
    @Test
    public void testCameraPostEventNoEmail() {
        File f = mock(File.class);
        CameraPostEvent cameraPostEvent = CommonTestEntityUtils.getCameraPostEvent();
        cameraPostEvent.setFile(f);
        Camera camera = CommonTestEntityUtils.getCamera("name", "zoneName");
        camera.setProcessPostEvents(false);
        User user = CommonTestEntityUtils.getUser();
        doNothing().when(cameraEventService).persistCameraPostEvent(cameraPostEvent);
        when(userLookupService.lookupUserByUserName("userName")).thenReturn(user);
        when(cameraEventService.lookupCameraByNameAndUser("name", "userName")).thenReturn(camera);
        cameraController.handleCameraPostEvent(cameraPostEvent);
        verify(cameraEventService, times(1)).persistCameraPostEvent(cameraPostEvent);
        verify(emailService, never()).sendFileAttachmentEmail(cameraPostEvent, "test@test.com");
        verify(f, times(1)).delete();
    }

    //given: camera post event
    //  and: user
    //  and: camera where post event can be processed
    //  and: fails persisting event to data store
    // when: handling the camera post event
    // then: expect the post event to be persisted
    //  and: email service to not send email
    //  and: file to be deleted
    @Test
    public void testCameraPostEventNoEmailDataStoreFailure() {
        File f = mock(File.class);
        CameraPostEvent cameraPostEvent = CommonTestEntityUtils.getCameraPostEvent();
        cameraPostEvent.setFile(f);
        Camera camera = CommonTestEntityUtils.getCamera("name", "zoneName");
        camera.setProcessPostEvents(false);
        User user = CommonTestEntityUtils.getUser();
        doThrow(new BusinessException()).when(cameraEventService).persistCameraPostEvent(cameraPostEvent);
        when(userLookupService.lookupUserByUserName("userName")).thenReturn(user);
        when(cameraEventService.lookupCameraByNameAndUser("name", "userName")).thenReturn(camera);
        cameraController.handleCameraPostEvent(cameraPostEvent);
        verify(emailService, times(1))
                .sendEmail("Unable to save video in persistent store", user.getEmailAddress(), "If emailing video was enabled, video attachment will be sent shortly");
        verify(cameraEventService, times(1)).persistCameraPostEvent(cameraPostEvent);
        verify(emailService, never()).sendFileAttachmentEmail(cameraPostEvent, "test@test.com");
        verify(f, times(1)).delete();
    }

    //given: camera post event
    //  and: user
    //  and: no camera found configured
    // when: handling the camera post event
    // then: expect the post event to be persisted
    //  and: email service to send email with attachment
    //  and: file to be deleted
    @Test
    public void testCameraPostEventSuccessNoCameraConfigured() {
        File f = mock(File.class);
        CameraPostEvent cameraPostEvent = CommonTestEntityUtils.getCameraPostEvent();
        cameraPostEvent.setFile(f);
        Camera camera = null;
        User user = CommonTestEntityUtils.getUser();
        doNothing().when(cameraEventService).persistCameraPostEvent(cameraPostEvent);
        when(userLookupService.lookupUserByUserName("userName")).thenReturn(user);
        when(cameraEventService.lookupCameraByNameAndUser("name", "userName")).thenReturn(camera);
        cameraController.handleCameraPostEvent(cameraPostEvent);
        verify(cameraEventService, times(1)).persistCameraPostEvent(cameraPostEvent);
        verify(emailService, times(1)).sendFileAttachmentEmail(cameraPostEvent, "test@test.com");
        verify(f, times(1)).delete();
    }


    //given: camera notify event
    //  and: user
    //  and: camera found configured
    // when: handling the notify event
    // then: expect the notify event to send an email
    @Test
    public void testCameraNotifyEventSuccess() {
        CameraNotifyEvent cameraNotifyEvent = CommonTestEntityUtils.getCameraNotifyEvent();
        User user = CommonTestEntityUtils.getUser();
        Camera camera = CommonTestEntityUtils.getCamera("name", "zoneName");
        when(userLookupService.lookupUserByUserName("userName")).thenReturn(user);
        when(cameraEventService.lookupCameraByNameAndUser("name", "user")).thenReturn(camera);
        cameraController.handleCameraNotifyEvent(cameraNotifyEvent);
        verify(emailService, times(1)).sendNotifyEmail("name");
    }

    //given: camera notify event
    //  and: user
    //  and: no camera found configured
    // when: handling the notify event
    // then: expect the notify event to send an email
    @Test
    public void testCameraNotifyEventSuccessNoCameraConfigured() {
        CameraNotifyEvent cameraNotifyEvent = CommonTestEntityUtils.getCameraNotifyEvent();
        User user = CommonTestEntityUtils.getUser();
        Camera camera = null;
        when(userLookupService.lookupUserByUserName("userName")).thenReturn(user);
        when(cameraEventService.lookupCameraByNameAndUser("name", "userName")).thenReturn(camera);
        cameraController.handleCameraNotifyEvent(cameraNotifyEvent);
        verify(emailService, times(1)).sendNotifyEmail("name");
    }

    //given: camera notify event
    //  and: user
    //  and: camera found configured
    //  and: process notify event not enabled
    // when: handling the notify event
    // then: expect the notify event to send an email
    @Test
    public void testCameraNotifyEventSuccessNoProcessEvents() {
        CameraNotifyEvent cameraNotifyEvent = CommonTestEntityUtils.getCameraNotifyEvent();
        User user = CommonTestEntityUtils.getUser();
        Camera camera = CommonTestEntityUtils.getCamera("name", "zoneName");
        camera.setProcessNotifyEvents(false);
        when(userLookupService.lookupUserByUserName("userName")).thenReturn(user);
        when(cameraEventService.lookupCameraByNameAndUser("name", "user")).thenReturn(camera);
        cameraController.handleCameraNotifyEvent(cameraNotifyEvent);
        verify(emailService, never()).sendNotifyEmail("name");
    }
}
