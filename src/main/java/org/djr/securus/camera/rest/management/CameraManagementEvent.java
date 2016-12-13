package org.djr.securus.camera.rest.management;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by djr4488 on 12/12/16.
 */
public abstract class CameraManagementEvent {
    private String cameraName;
    private Long userId;

    public CameraManagementEvent() {
    }

    public CameraManagementEvent(String cameraName, Long userId) {
        this.cameraName = cameraName;
        this.userId = userId;
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
