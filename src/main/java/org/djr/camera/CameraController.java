package org.djr.camera;

import org.djr.camera.messaging.email.EmailService;
import org.djr.camera.services.CameraEventService;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

/**
 * Created by djr4488 on 11/22/16.
 */
@ApplicationScoped
public class CameraController {
    @Inject
    private Logger log;
    @Inject
    private EmailService emailService;
    @Inject
    private CameraEventService cameraEventService;

    public void handleCameraPostEvent(@Observes CameraPostEvent cameraPostEvent) {
        log.debug("handleCameraPostEvent() cameraPostEvent:{}", cameraPostEvent);
        try {
            cameraEventService.persistCameraPostEvent(cameraPostEvent);
        } catch (Exception ex) {
            log.error("handleCameraPostEvent() cameraPostEvent:{} failed to write to database with exception:{}", cameraPostEvent, ex);
        }
        emailService.sendFileAttachmentEmail(cameraPostEvent);
        cameraPostEvent.getFile().delete();
    }

    public void handleCameraNotifyEvent(@Observes CameraNotifyEvent cameraNotifyEvent) {
        log.debug("handleCameraNotifyEvent() cameraNotifyEvent:{}", cameraNotifyEvent);
        emailService.sendNotifyEmail(cameraNotifyEvent.getCameraName());
    }
}
