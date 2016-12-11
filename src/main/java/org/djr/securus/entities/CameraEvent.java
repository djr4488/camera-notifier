package org.djr.securus.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.File;
import java.util.Date;

/**
 * Created by djr4488 on 12/8/16.
 */
@Entity
@Table(name = "camera_events")
public class CameraEvent extends Identifiable {
    @Column(name = "filename")
    private String filename;
    @Column(name = "content_type")
    private String contentType;
    @Column(name = "context_length")
    private Long contentLength;
    @Column(name = "x_timestamped_file")
    @Temporal(TemporalType.TIMESTAMP)
    private Date xTimestampedFile;
    @Column(name = "x_trigger_type")
    private String xTriggerType;
    @Column(name = "x_event_info")
    private String xEventInfo;
    @Column(name = "host")
    private String host;
    @Column(name = "file")
    private File file;
    @ManyToOne
    @JoinColumn(name = "camera_id", referencedColumnName = "id")
    private Camera camera;

    public CameraEvent() {
    }

    public CameraEvent(String filename, String contentType, Long contentLength, Date xTimestampedFile, String xTriggerType, String xEventInfo, String host, File file) {
        this.filename = filename;
        this.contentType = contentType;
        this.contentLength = contentLength;
        this.xTimestampedFile = xTimestampedFile;
        this.xTriggerType = xTriggerType;
        this.xEventInfo = xEventInfo;
        this.host = host;
        this.file = file;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getContentLength() {
        return contentLength;
    }

    public void setContentLength(Long contentLength) {
        this.contentLength = contentLength;
    }

    public Date getxTimestampedFile() {
        return xTimestampedFile;
    }

    public void setxTimestampedFile(Date xTimestampedFile) {
        this.xTimestampedFile = xTimestampedFile;
    }

    public String getxTriggerType() {
        return xTriggerType;
    }

    public void setxTriggerType(String xTriggerType) {
        this.xTriggerType = xTriggerType;
    }

    public String getxEventInfo() {
        return xEventInfo;
    }

    public void setxEventInfo(String xEventInfo) {
        this.xEventInfo = xEventInfo;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }
}
