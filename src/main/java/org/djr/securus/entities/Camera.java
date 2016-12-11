package org.djr.securus.entities;

import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

/**
 * Created by djr4488 on 12/6/16.
 */
@Entity
@Table(name = "cameras")
@NamedQueries(
        @NamedQuery(name = "findByCameraNameAndUserName",
                    query = "select camera from Camera camera " +
                            "where camera.cameraName = :cameraName and camera.user.userName = :userName")
)
public class Camera extends Identifiable {
    private static final long serialVersionUID = 1L;
    @Column(name = "camera_name", nullable = false)
    private String cameraName;
    // the following three fields used to trigger an http event on the securus to capture a video/audio stream
    @Column(name = "camera_url", nullable = true)
    private String cameraTriggerUrl;
    @Column(name = "camera_username", nullable = true)
    private String cameraAdministrator;
    @Column(name = "camera_password", nullable = true)
    private String cameraPassword;
    @Column(name = "camera_zone", nullable = true)
    private String cameraZone;
    @Column(name = "camera_process_event_http_post", nullable = false)
    private boolean processPostEvents;
    @Column(name = "camera_process_event_http_notify", nullable = false)
    private boolean processNotifyEvents;
    //currently ignored as i don't yet support sms, but I think it would be kind of neat to do so in the future
    @Column(name = "send_notify_event_as_sms", nullable = false)
    private boolean sendNotifyEventAsSms;
    @Column(name = "send_notify_event_as_email", nullable = false)
    private boolean sendNotifyEventAsEmail;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @OneToMany
    @JoinColumn(name = "camera_id", referencedColumnName = "id")
    private List<CameraEvent> cameraEvents;

    public Camera() {
        Date now = DateTime.now().toDate();
        this.setCreatedAt(now);
        this.setLastUpdatedAt(now);
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public String getCameraTriggerUrl() {
        return cameraTriggerUrl;
    }

    public void setCameraTriggerUrl(String cameraTriggerUrl) {
        this.cameraTriggerUrl = cameraTriggerUrl;
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

    public String getCameraZone() {
        return cameraZone;
    }

    public void setCameraZone(String cameraZone) {
        this.cameraZone = cameraZone;
    }

    public boolean isProcessPostEvents() {
        return processPostEvents;
    }

    public void setProcessPostEvents(boolean processPostEvents) {
        this.processPostEvents = processPostEvents;
    }

    public boolean isProcessNotifyEvents() {
        return processNotifyEvents;
    }

    public void setProcessNotifyEvents(boolean processNotifyEvents) {
        this.processNotifyEvents = processNotifyEvents;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<CameraEvent> getCameraEvents() {
        return cameraEvents;
    }

    public void setCameraEvents(List<CameraEvent> cameraEvents) {
        this.cameraEvents = cameraEvents;
    }
}
