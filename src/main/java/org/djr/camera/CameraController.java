package org.djr.camera;

import org.djr.camera.messaging.email.EmailService;
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

    public void handleCameraPostEvent(@Observes CameraPostEvent cameraPostEvent) {
        log.debug("handleCameraPostEvent() cameraPostEvent:{}", cameraPostEvent);
        emailService.sendFileAttachmentEmail(cameraPostEvent);
        cameraPostEvent.getFile().delete();
    }

    public void handleCameraNotifyEvent(@Observes CameraNotifyEvent cameraNotifyEvent) {
        log.debug("handleCameraNotifyEvent() cameraNotifyEvent:{}", cameraNotifyEvent);
        emailService.sendNotifyEmail(cameraNotifyEvent.getCameraName());
    }
}
