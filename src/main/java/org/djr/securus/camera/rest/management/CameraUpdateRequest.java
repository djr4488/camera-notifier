package org.djr.securus.camera.rest.management;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by djr4488 on 12/13/16.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class CameraUpdateRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    @XmlElement
    private String cameraName;
    @XmlElement
    private String cameraAdministrator;
    @XmlElement
    private String cameraPassword;
    @XmlElement
    private boolean processNotifyEvents;
    @XmlElement
    private boolean sendNotifyEventAsEmail;
    @XmlElement
    private boolean sendNotifyEventAsSms;
    @XmlElement
    private boolean processPostEvents;
    @XmlElement
    private String cameraTriggerUrl;
    @XmlElement
    private String cameraZone;


    public CameraUpdateRequest() {
    }

    public CameraUpdateRequest(String cameraName, String cameraAdministrator, String cameraPassword, boolean processNotifyEvents,
                            boolean sendNotifyEventAsEmail, boolean sendNotifyEventAsSms, boolean processPostEvents,
                            String cameraTriggerUrl, String cameraZone) {
        this.cameraName = cameraName;
        this.cameraAdministrator = cameraAdministrator;
        this.cameraPassword = cameraPassword;
        this.processNotifyEvents = processNotifyEvents;
        this.sendNotifyEventAsEmail = sendNotifyEventAsEmail;
        this.sendNotifyEventAsSms = sendNotifyEventAsSms;
        this.processPostEvents = processPostEvents;
        this.cameraTriggerUrl = cameraTriggerUrl;
        this.cameraZone = cameraZone;
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

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
