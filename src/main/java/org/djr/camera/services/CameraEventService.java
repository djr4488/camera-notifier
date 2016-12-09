package org.djr.camera.services;

import org.djr.camera.CameraPostEvent;
import org.djr.camera.CameraUtilities;
import org.djr.camera.entities.Camera;
import org.djr.camera.entities.CameraEvent;
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
        TypedQuery<Camera> cameraQuery = em.createNamedQuery("findByCameraNameAndUserName", Camera.class);
        cameraQuery.setParameter("cameraName", cameraPostEvent.getCameraName());
        cameraQuery.setParameter("userName", cameraPostEvent.getUserName());
        Camera camera = cameraQuery.getSingleResult();
        cameraEvent.setCamera(camera);
        em.persist(cameraEvent);
    }
}
