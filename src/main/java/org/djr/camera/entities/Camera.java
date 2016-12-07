package org.djr.camera.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by djr4488 on 12/6/16.
 */
@Entity
@Table(name = "cameras")
public class Camera extends Identifiable {
    private static final long serialVersionUID = 1L;
    @Column(name = "camera_name", nullable = false)
    private String cameraName;
    // the following three are place holders if I am able to figure out how to use the http_trigger.cgi
    // if not, these are pretty pointless
    @Column(name = "camera_url", nullable = true)
    private String cameraUrl;
    @Column(name = "camera_username", nullable = true)
    private String cameraUserName;
    @Column(name = "camera_password", nullable = true)
    private String cameraPassword;
    @Column(name = "camera_process_event_http_post", nullable = false)
    private boolean cameraProcessEventHttpPost;
    @Column(name = "camera_process_event_http_notify", nullable = false)
    private boolean cameraProcessEventHttpNotify;
    //currently ignored as i don't yet support sms, but I think it would be kind of neat to do so in the future
    @Column(name = "send_notify_event_as_sms", nullable = false)
    private boolean sendNotifyEventAsSms;
    @Column(name = "send_notify_event_as_email", nullable = false)
    private boolean sendNotifyEventAsEmail;

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public String getCameraUrl() {
        return cameraUrl;
    }

    public void setCameraUrl(String cameraUrl) {
        this.cameraUrl = cameraUrl;
    }

    public String getCameraUserName() {
        return cameraUserName;
    }

    public void setCameraUserName(String cameraUserName) {
        this.cameraUserName = cameraUserName;
    }

    public String getCameraPassword() {
        return cameraPassword;
    }

    public void setCameraPassword(String cameraPassword) {
        this.cameraPassword = cameraPassword;
    }

    public boolean isCameraProcessEventHttpPost() {
        return cameraProcessEventHttpPost;
    }

    public void setCameraProcessEventHttpPost(boolean cameraProcessEventHttpPost) {
        this.cameraProcessEventHttpPost = cameraProcessEventHttpPost;
    }

    public boolean isCameraProcessEventHttpNotify() {
        return cameraProcessEventHttpNotify;
    }

    public void setCameraProcessEventHttpNotify(boolean cameraProcessEventHttpNotify) {
        this.cameraProcessEventHttpNotify = cameraProcessEventHttpNotify;
    }

    public boolean isSendNotifyEventAsSms() {
        return sendNotifyEventAsSms;
    }

    public void setSendNotifyEventAsSms(boolean sendNotifyEventAsSms) {
        this.sendNotifyEventAsSms = sendNotifyEventAsSms;
    }

    public boolean isSendNotifyEventAsEmail() {
        return sendNotifyEventAsEmail;
    }

    public void setSendNotifyEventAsEmail(boolean sendNotifyEventAsEmail) {
        this.sendNotifyEventAsEmail = sendNotifyEventAsEmail;
    }
}
