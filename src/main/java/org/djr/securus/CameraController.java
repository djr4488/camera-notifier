package org.djr.securus;

import com.codahale.metrics.annotation.Timed;
import org.djr.securus.entities.Camera;
import org.djr.securus.entities.User;
import org.djr.securus.exceptions.BusinessException;
import org.djr.securus.messaging.email.EmailService;
import org.djr.securus.camera.CameraEventService;
import org.djr.securus.user.UserLookupService;
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
    @Inject
    private UserLookupService userLookupService;

    @Timed
    public void handleCameraPostEvent(@Observes CameraPostEvent cameraPostEvent) {
        log.debug("handleCameraPostEvent() cameraPostEvent:{}", cameraPostEvent);
        User user = userLookupService.lookupUserByUserName(cameraPostEvent.getUserName());
        try {
            cameraEventService.persistCameraPostEvent(cameraPostEvent);
        } catch (BusinessException ex) {
            log.error("handleCameraPostEvent() cameraPostEvent:{} failed to write to database with exception:{}", cameraPostEvent, ex);
            emailService.sendEmail("Unable to save video in persistent store", user.getEmailAddress(), "If emailing video was enabled, video attachment will be sent shortly");
        }
        if (isCameraPostEventAllowed(cameraPostEvent.getCameraName(), cameraPostEvent.getUserName())) {
            emailService.sendFileAttachmentEmail(cameraPostEvent, user.getEmailAddress());
        }
        cameraPostEvent.getFile().delete();
    }

    public void handleCameraNotifyEvent(@Observes CameraNotifyEvent cameraNotifyEvent) {
        log.debug("handleCameraNotifyEvent() cameraNotifyEvent:{}", cameraNotifyEvent);
        if (isCameraNotifyEventAllowed(cameraNotifyEvent.getCameraName(), cameraNotifyEvent.getUserName())) {
            emailService.sendNotifyEmail(cameraNotifyEvent.getCameraName());
        }
    }

    public boolean isCameraPostEventAllowed(String cameraName, String userName) {
        boolean sendEmail = false;
        Camera camera = cameraEventService.lookupCameraByNameAndUser(cameraName, userName);
        if (null == camera || camera.isProcessPostEvents()) {
            sendEmail = true;
        }
        return sendEmail;
    }

    public boolean isCameraNotifyEventAllowed(String cameraName, String userName) {
        boolean sendEmail = false;
        Camera camera = cameraEventService.lookupCameraByNameAndUser(cameraName, userName);
        if (null == camera || camera.isProcessNotifyEvents()) {
            sendEmail = true;
        }
        return sendEmail;
    }
}
