package org.djr.securus;

import junit.framework.TestCase;
import org.djr.securus.camera.CameraHttpTriggerService;
import org.djr.securus.camera.rest.trigger.TriggerEvent;
import org.djr.securus.cdi.logs.LoggerProducer;
import org.djr.securus.entities.User;
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

import java.util.ArrayList;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by djr4488 on 12/19/16.
 */
@RunWith(CdiRunner.class)
@AdditionalClasses({LoggerProducer.class})
public class TriggerControllerTest extends TestCase {
    @Mock
    @Produces
    private UserLookupService userLookupService;
    @Mock
    @Produces
    private CameraHttpTriggerService cameraHttpTriggerService;
    @Inject
    private TriggerController triggerController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    //given: zone name
    //  and: user id
    // when: handling sensor event
    // then: expect no exceptions
    //  and: operation success
    @Test
    public void testHandleSensorEventCameraFoundTriggerSucceeds() {
        TriggerEvent triggerEvent = CommonTestEntityUtils.getTriggerEvent();
        User user = CommonTestEntityUtils.getUser();
        user.setCameras(CommonTestEntityUtils.getCameraList());
        when(userLookupService.lookUserById(1L)).thenReturn(user);
        when(cameraHttpTriggerService.doCameraTrigger("admin", "password", "triggerUrl")).thenReturn(true);
        triggerController.handleSensorEvent(triggerEvent);
        verify(userLookupService, times(1)).lookUserById(1L);
        verify(cameraHttpTriggerService, times(1)).doCameraTrigger("admin", "password", "triggerUrl");
    }

    //given: zone name
    //  and: user id
    //  and: user has no cameras
    // when: handling trigger event
    // then: expect no camera notified
    @Test
    public void testHandleSensorEventNoCamerasForUser() {
        TriggerEvent triggerEvent = CommonTestEntityUtils.getTriggerEvent();
        User user = CommonTestEntityUtils.getUser();
        user.setCameras(new ArrayList<>());
        when(userLookupService.lookUserById(1L)).thenReturn(user);
        triggerController.handleSensorEvent(triggerEvent);
        verify(userLookupService, times(1)).lookUserById(1L);
        verify(cameraHttpTriggerService, never()).doCameraTrigger("admin", "password", "triggerUrl");
    }

    //given: zone name
    //  and: user id
    //  and: user has cameras
    //  and: no camera matches zone name
    // when: handling trigger event
    // then: expect no camera notified
    @Test
    public void testHandleSensorEventNoCameraMatchesZoneName() {
        TriggerEvent triggerEvent = CommonTestEntityUtils.getTriggerEvent();
        triggerEvent.setZoneName("zoneName3");
        User user = CommonTestEntityUtils.getUser();
        user.setCameras(CommonTestEntityUtils.getCameraList());
        when(userLookupService.lookUserById(1L)).thenReturn(user);
        when(cameraHttpTriggerService.doCameraTrigger("admin", "password", "triggerUrl")).thenReturn(true);
        triggerController.handleSensorEvent(triggerEvent);
        verify(userLookupService, times(1)).lookUserById(1L);
        verify(cameraHttpTriggerService, never()).doCameraTrigger("admin", "password", "triggerUrl");
    }

    //given: zone name
    //  and: user id
    // when: handling sensor event
    // then: expect no exceptions
    //  and: operation fails
    @Test
    public void testHandleSensorEventCameraFoundTriggerFails() {
        TriggerEvent triggerEvent = CommonTestEntityUtils.getTriggerEvent();
        User user = CommonTestEntityUtils.getUser();
        user.setCameras(CommonTestEntityUtils.getCameraList());
        when(userLookupService.lookUserById(1L)).thenReturn(user);
        when(cameraHttpTriggerService.doCameraTrigger("admin", "password", "triggerUrl")).thenReturn(false);
        triggerController.handleSensorEvent(triggerEvent);
        verify(userLookupService, times(1)).lookUserById(1L);
        verify(cameraHttpTriggerService, times(1)).doCameraTrigger("admin", "password", "triggerUrl");
    }

}
