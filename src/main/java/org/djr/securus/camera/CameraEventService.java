package org.djr.securus.camera;

import org.djr.securus.CameraPostEvent;
import org.djr.securus.CameraUtilities;
import org.djr.securus.entities.Camera;
import org.djr.securus.entities.CameraEvent;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

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
        }
    }

    public Camera lookupCameraByNameAndUser(String cameraName, String userName) {
        TypedQuery<Camera> cameraQuery = em.createNamedQuery("findByCameraNameAndUserName", Camera.class);
        cameraQuery.setParameter("cameraName", cameraName);
        cameraQuery.setParameter("userName", userName);
        Camera camera = cameraQuery.getSingleResult();
        return camera;
    }
}
