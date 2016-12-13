package org.djr.securus.camera.rest.management;

/**
 * Created by djr4488 on 12/12/16.
 */
public class DeleteCameraEvent extends CameraManagementEvent {

    public DeleteCameraEvent() {
    }

    public DeleteCameraEvent(String cameraName, Long userId) {
        this.setCameraName(cameraName);
        this.setUserId(userId);
    }
}
