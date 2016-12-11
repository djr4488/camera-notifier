package org.djr.securus;

import org.djr.securus.camera.rest.trigger.TriggerEvent;
import org.djr.securus.entities.Camera;
import org.djr.securus.entities.User;
import org.djr.securus.camera.CameraHttpTriggerService;
import org.djr.securus.user.UserLookupService;
import org.slf4j.Logger;


import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

/**
 * Created by djr4488 on 12/7/16.
 */
@ApplicationScoped
public class TriggerController {
    @Inject
    private Logger log;
    @Inject
    private UserLookupService userLookupService;
    @Inject
    private CameraHttpTriggerService cameraHttpTriggerService;

    public void handleSensorEvent(@Observes TriggerEvent triggerEvent) {
        log.debug("handleSensorEvent() triggerEvent:{}", triggerEvent);
        User user = userLookupService.lookUserById(triggerEvent.getUserId());
        Camera camera = getCameraByZoneNameAndUser(triggerEvent.getZoneName(), user);
        if (null != camera) {
            doHttpTrigger(camera);
        } else {
            log.debug("handleSensorEvent() zone:{} not found", triggerEvent.getZoneName());
        }
    }

    private Camera getCameraByZoneNameAndUser(String zoneName, User user) {
        for (Camera camera : user.getCameras()) {
            if (zoneName.trim().equalsIgnoreCase(camera.getCameraZone().trim())) {
                return camera;
            }
        }
        return null;
    }

    private void doHttpTrigger(Camera camera) {
        boolean successful =
                cameraHttpTriggerService.doCameraTrigger(camera.getCameraAdministrator(), camera.getCameraPassword(), camera.getCameraTriggerUrl());
        if (!successful) {
            //TODO do throw exception handle it in exception mapper? or email user?
        }
    }
}
