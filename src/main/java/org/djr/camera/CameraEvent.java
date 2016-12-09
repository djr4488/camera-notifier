package org.djr.camera;

/**
 * Created by djr4488 on 12/4/16.
 */
public abstract class CameraEvent {
    private String cameraName;
    private String host;
    private String authorization;
    private String userName;
    private String password;
    public CameraEvent() {
    }

    public CameraEvent(String cameraName, String host, String authorization, String userName, String password) {
        this.cameraName = cameraName;
        this.host = host;
        this.authorization = authorization;
        this.userName = userName;
        this.password = password;
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "CameraEvent{" +
                "cameraName='" + cameraName + '\'' +
                '}';
    }
}
