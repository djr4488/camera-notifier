package org.djr.securus.camera;

import org.apache.commons.beanutils.BeanUtils;
import org.djr.securus.CameraPostEvent;
import org.djr.securus.CameraUtilities;
import org.djr.securus.camera.rest.management.AddCameraEvent;
import org.djr.securus.camera.rest.management.CameraManagementEvent;
import org.djr.securus.camera.rest.management.DeleteCameraEvent;
import org.djr.securus.camera.rest.management.UpdateCameraEvent;
import org.djr.securus.entities.Camera;
import org.djr.securus.entities.CameraEvent;
import org.djr.securus.entities.User;
import org.djr.securus.exceptions.BusinessException;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by djr4488 on 12/8/16.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class CameraEventService {
    @Inject
    private Logger log;
    @PersistenceContext(unitName = "camera_notifier")
    private EntityManager em;

    public void persistCameraPostEvent(CameraPostEvent cameraPostEvent) {
        log.debug("persistCameraPostEvent() cameraPostEvent:{}", cameraPostEvent);
        DateTime eventTime = CameraUtilities.getDateTime(cameraPostEvent);
        CameraEvent cameraEvent = new CameraEvent(cameraPostEvent.getFile().getName(), cameraPostEvent.getContentType(),
                Long.parseLong(cameraPostEvent.getContentLength()), eventTime.toDate(), cameraPostEvent.getxTriggerType(),
                cameraPostEvent.getxEventInfo(), cameraPostEvent.getHost(), cameraPostEvent.getFile());
        DateTime now = DateTime.now();
        cameraEvent.setCreatedAt(now.toDate());
        cameraEvent.setLastUpdatedAt(now.toDate());
        try {
            Camera camera = lookupCameraByNameAndUser(cameraPostEvent.getCameraName(), cameraPostEvent.getUserName());
            cameraEvent.setCamera(camera);
            em.persist(cameraEvent);
        } catch (Exception ex) {
            log.debug("persistCameraPostEvent() didn't find or couldn't save camera event", ex);
            throw new BusinessException("failed to persist video to data store");
        }
    }

    public Camera lookupCameraByNameAndUser(String cameraName, String userName) {
        log.debug("lookupCameraByNameAndUser() cameraName:{}, userName:{}", cameraName, userName);
        TypedQuery<Camera> cameraQuery = em.createNamedQuery("findByCameraNameAndUserName", Camera.class);
        cameraQuery.setParameter("cameraName", cameraName);
        cameraQuery.setParameter("userName", userName);
        Camera camera = cameraQuery.getSingleResult();
        return camera;
    }

    public void addCamera(AddCameraEvent addCameraEvent, User user) {
        log.debug("addCamera() addCameraEvent:{}", addCameraEvent);
        Camera camera = new Camera();
        setCameraParametersFromCameraEvent(camera, addCameraEvent);
        camera.setUser(user);
        em.persist(camera);
    }

    public void deleteCamera(DeleteCameraEvent deleteCameraEvent, User user) {
        log.debug("deleteCamera() deleteCameraEvent:{}", deleteCameraEvent);
        TypedQuery<Camera> cameraQuery = em.createNamedQuery("deleteCamera", Camera.class);
        cameraQuery.setParameter("cameraName", deleteCameraEvent.getCameraName());
        cameraQuery.setParameter("user", user);
        cameraQuery.executeUpdate();
    }

    public void updateCamera(UpdateCameraEvent updateCameraEvent, User user) {
        log.debug("updateCamera() updateCameraEvent:{}", updateCameraEvent);
        Camera camera = lookupCameraByNameAndUser(updateCameraEvent.getCameraName(), user.getUserName());
        setCameraParametersFromCameraEvent(camera, updateCameraEvent);
        em.merge(camera);
    }

    private <T extends CameraManagementEvent> void setCameraParametersFromCameraEvent(Camera camera, T cameraEvent) {
        camera.setCameraName(cameraEvent.getCameraName());
        camera.setCameraAdministrator(cameraEvent.getCameraAdministrator());
        camera.setCameraPassword(cameraEvent.getCameraPassword());
        camera.setProcessNotifyEvents(cameraEvent.isProcessNotifyEvents());
        camera.setSendNotifyEventAsEmail(cameraEvent.isSendNotifyEventAsEmail());
        camera.setSendNotifyEventAsSms(cameraEvent.isSendNotifyEventAsSms());
        camera.setProcessPostEvents(cameraEvent.isProcessPostEvents());
        camera.setCameraTriggerUrl(cameraEvent.getCameraTriggerUrl());
        camera.setCameraZone(cameraEvent.getCameraZone());
        if (cameraEvent instanceof UpdateCameraEvent) {
            camera.setLastUpdatedAt(DateTime.now().toDate());
        }
    }
}
