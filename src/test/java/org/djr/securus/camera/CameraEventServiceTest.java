package org.djr.securus.camera;

import junit.framework.TestCase;
import org.djr.securus.CameraPostEvent;
import org.djr.securus.CommonTestEntityUtils;
import org.djr.securus.camera.rest.management.AddCameraEvent;
import org.djr.securus.camera.rest.management.DeleteCameraEvent;
import org.djr.securus.camera.rest.management.UpdateCameraEvent;
import org.djr.securus.entities.Camera;
import org.djr.securus.entities.CameraEvent;
import org.djr.securus.entities.User;
import org.djr.securus.exceptions.BusinessException;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import java.io.File;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by djr4488 on 1/4/17.
 */
@RunWith(CdiRunner.class)
public class CameraEventServiceTest extends TestCase {
    @Mock
    private Logger log;
    @Mock
    private EntityManager em;
    @Mock
    private TypedQuery<Camera> cameraQuery;
    @InjectMocks
    private CameraEventService cameraEventService = new CameraEventService();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPersistCameraPostEventSuccessOnPersist() {
        File f = mock(File.class);
        Camera camera = CommonTestEntityUtils.getCamera("name", "zone");
        CameraPostEvent cameraPostEvent = CommonTestEntityUtils.getCameraPostEvent();
        cameraPostEvent.setxTimeStampedFile("170101000000");
        cameraPostEvent.setContentLength("100");
        cameraPostEvent.setFile(f);
        when(em.createNamedQuery("findByCameraNameAndUserName", Camera.class)).thenReturn(cameraQuery);
        when(cameraQuery.getSingleResult()).thenReturn(camera);
        when(f.getName()).thenReturn("fileName.avi");
        cameraEventService.persistCameraPostEvent(cameraPostEvent);
        verify(em, times(1)).persist(any(CameraEvent.class));
    }

    @Test(expected = BusinessException.class)
    public void testPersistCameraPostEventNoResultExceptionOnPersist() {
        File f = mock(File.class);
        CameraPostEvent cameraPostEvent = CommonTestEntityUtils.getCameraPostEvent();
        cameraPostEvent.setxTimeStampedFile("170101000000");
        cameraPostEvent.setContentLength("100");
        cameraPostEvent.setFile(f);
        when(em.createNamedQuery("findByCameraNameAndUserName", Camera.class)).thenReturn(cameraQuery);
        when(cameraQuery.getSingleResult()).thenThrow(new NoResultException("test"));
        when(f.getName()).thenReturn("fileName.avi");
        cameraEventService.persistCameraPostEvent(cameraPostEvent);
        verify(em, never()).persist(any(CameraEvent.class));
    }

    @Test
    public void testAddCamera() {
        AddCameraEvent addCameraEvent = CommonTestEntityUtils.getAddCameraEvent();
        User user = CommonTestEntityUtils.getUser();
        ArgumentCaptor<Camera> cameraArgumentCaptor = ArgumentCaptor.forClass(Camera.class);
        cameraEventService.addCamera(addCameraEvent, user);
        verify(em, times(1)).persist(cameraArgumentCaptor.capture());
        Camera camera = cameraArgumentCaptor.getValue();
        assertNotNull(camera);
        assertEquals(addCameraEvent.getCameraName(), camera.getCameraName());
        assertEquals(user.getUserName(), camera.getUser().getUserName());
    }

    @Test
    public void testDeleteCamera() {
        DeleteCameraEvent deleteCameraEvent = CommonTestEntityUtils.getDeleteCameraEvent();
        User user = CommonTestEntityUtils.getUser();
        when(em.createNamedQuery("deleteCamera", Camera.class)).thenReturn(cameraQuery);
        cameraEventService.deleteCamera(deleteCameraEvent, user);
        verify(cameraQuery, times(1)).executeUpdate();
    }

    @Test
    public void testUpdateCamera() {
        UpdateCameraEvent updateCameraEvent = CommonTestEntityUtils.getUpdateCameraEvent();
        User user = CommonTestEntityUtils.getUser();
        Camera cameraLookup = CommonTestEntityUtils.getCamera("name", "zone");
        cameraLookup.setUser(user);
        ArgumentCaptor<Camera> cameraArgumentCaptor = ArgumentCaptor.forClass(Camera.class);
        when(em.createNamedQuery("findByCameraNameAndUserName", Camera.class)).thenReturn(cameraQuery);
        when(cameraQuery.getSingleResult()).thenReturn(cameraLookup);
        cameraEventService.updateCamera(updateCameraEvent, user);
        verify(em, times(1)).merge(cameraArgumentCaptor.capture());
        Camera camera = cameraArgumentCaptor.getValue();
        assertNotNull(camera);
        assertEquals(updateCameraEvent.getCameraName(), camera.getCameraName());
        assertEquals(user.getUserName(), camera.getUser().getUserName());
    }
}
