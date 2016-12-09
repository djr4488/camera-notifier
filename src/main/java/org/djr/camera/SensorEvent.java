package org.djr.camera;

/**
 * Created by djr4488 on 12/7/16.
 */
public class SensorEvent {
    private  String userName;
    private String sensorZone;

    public SensorEvent() {
    }

    public SensorEvent(String userName, String sensorZone) {
        this.userName = userName;
        this.sensorZone = sensorZone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSensorZone() {
        return sensorZone;
    }

    public void setSensorZone(String sensorZone) {
        this.sensorZone = sensorZone;
    }
}
