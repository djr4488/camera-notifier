package org.djr.securus.camera.rest.management;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by djr4488 on 12/13/16.
 */
public class UpdateCameraEvent extends CameraManagementEvent {
    private String cameraAdministrator;
    private String cameraPassword;
    private boolean processNotifyEvents;
    private boolean sendNotifyEventAsEmail;
    private boolean sendNotifyEventAsSms;
    private boolean processPostEvents;
    private String cameraTriggerUrl;
    private String cameraZone;

    public String getCameraAdministrator() {
        return cameraAdministrator;
    }

    public void setCameraAdministrator(String cameraAdministrator) {
        this.cameraAdministrator = cameraAdministrator;
    }

    public String getCameraPassword() {
        return cameraPassword;
    }

    public void setCameraPassword(String cameraPassword) {
        this.cameraPassword = cameraPassword;
    }

    public boolean isProcessNotifyEvents() {
        return processNotifyEvents;
    }

    public void setProcessNotifyEvents(boolean processNotifyEvents) {
        this.processNotifyEvents = processNotifyEvents;
    }

    public boolean isSendNotifyEventAsEmail() {
        return sendNotifyEventAsEmail;
    }

    public void setSendNotifyEventAsEmail(boolean sendNotifyEventAsEmail) {
        this.sendNotifyEventAsEmail = sendNotifyEventAsEmail;
    }

    public boolean isSendNotifyEventAsSms() {
        return sendNotifyEventAsSms;
    }

    public void setSendNotifyEventAsSms(boolean sendNotifyEventAsSms) {
        this.sendNotifyEventAsSms = sendNotifyEventAsSms;
    }

    public boolean isProcessPostEvents() {
        return processPostEvents;
    }

    public void setProcessPostEvents(boolean processPostEvents) {
        this.processPostEvents = processPostEvents;
    }

    public String getCameraTriggerUrl() {
        return cameraTriggerUrl;
    }

    public void setCameraTriggerUrl(String cameraTriggerUrl) {
        this.cameraTriggerUrl = cameraTriggerUrl;
    }

    public String getCameraZone() {
        return cameraZone;
    }

    public void setCameraZone(String cameraZone) {
        this.cameraZone = cameraZone;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
