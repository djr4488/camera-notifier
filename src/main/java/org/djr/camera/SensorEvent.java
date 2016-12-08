package org.djr.camera;

/**
 * Created by djr4488 on 12/7/16.
 */
public class SensorEvent {
    private  String userName;
    private String cameraName;

    public SensorEvent() {
    }

    public SensorEvent(String userName, String cameraName) {
        this.userName = userName;
        this.cameraName = cameraName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }
}
