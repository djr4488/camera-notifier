package org.djr.camera;

/**
 * Created by djr4488 on 12/4/16.
 */
public class CameraNotifyEvent extends CameraEvent {
    public CameraNotifyEvent() {
    }

    public CameraNotifyEvent(String cameraName, String host, String authorization, String userName, String password) {
        super(cameraName, host, authorization, userName, password);
    }
}
