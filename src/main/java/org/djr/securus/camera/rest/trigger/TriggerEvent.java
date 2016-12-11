package org.djr.securus.camera.rest.trigger;

/**
 * Created by djr4488 on 12/7/16.
 */
public class TriggerEvent {
    private  Long userId;
    private String zoneName;

    public TriggerEvent() {
    }

    public TriggerEvent(Long userId, String zoneName) {
        this.userId = userId;
        this.zoneName = zoneName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userName) {
        this.userId = userName;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }
}
