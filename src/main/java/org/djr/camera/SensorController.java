package org.djr.camera;

import org.djr.camera.entities.Camera;
import org.djr.camera.entities.User;
import org.djr.camera.services.CameraHttpTriggerService;
import org.djr.camera.services.UserLookupService;
import org.slf4j.Logger;


import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

/**
 * Created by djr4488 on 12/7/16.
 */
@ApplicationScoped
public class SensorController {
    @Inject
    private Logger log;
    @Inject
    private UserLookupService userLookupService;
    @Inject
    private CameraHttpTriggerService cameraHttpTriggerService;

    public void handleSensorEvent(@Observes SensorEvent sensorEvent) {
        log.debug("handleSensorEvent() sensorEvent:{}", sensorEvent);
        //TODO look up user for now hard code something to test plumbing
        User user = userLookupService.lookupUserByUserName(sensorEvent.getUserName());
        Camera camera = getCameraByName(sensorEvent.getCameraName(), user);
        if (null != camera) {
            doHttpTrigger(camera);
        } else {
            log.debug("handleSensorEvent() camera:{} not found", sensorEvent.getCameraName());
        }
    }

    private Camera getCameraByName(String cameraName, User user) {
        for (Camera camera : user.getCameras()) {
            if (cameraName.trim().equalsIgnoreCase(camera.getCameraName().trim())) {
                return camera;
            }
        }
        return null;
    }

    private void doHttpTrigger(Camera camera) {
        boolean successful = cameraHttpTriggerService.doCameraTrigger(camera.getCameraUserName(), camera.getCameraPassword());
        if (!successful) {
            //TODO do throw exception handle it in exception mapper
        }
    }
}
