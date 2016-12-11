package org.djr.securus;

import java.io.File;

/**
 * Created by djr4488 on 12/4/16.
 */
public class CameraPostEvent extends CameraEvent {
    private File file;
    private String contentType;
    private String contentLength;
    private String xTimeStampedFile;
    //have seen MOTION; PIR
    private String xTriggerType;
    //if trigger type motion; motion,74,md_window0
    private String xEventInfo;

    public CameraPostEvent() {

    }

    public CameraPostEvent(String cameraName, String host, String authorization, String userName, String password,
                           File file, String contentType, String contentLength, String xTimeStampedFile, String xTriggerType,
                           String xEventInfo) {
        super(cameraName, host, authorization, userName, password);
        this.file = file;
        this.contentType = contentType;
        this.contentLength = contentLength;
        this.xTimeStampedFile = xTimeStampedFile;
        this.xTriggerType = xTriggerType;
        this.xEventInfo = xEventInfo;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentLength() {
        return contentLength;
    }

    public void setContentLength(String contentLength) {
        this.contentLength = contentLength;
    }

    public String getxTimeStampedFile() {
        return xTimeStampedFile;
    }

    public void setxTimeStampedFile(String xTimeStampedFile) {
        this.xTimeStampedFile = xTimeStampedFile;
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

    @Override
    public String toString() {
        return "CameraPostEvent{" +
                "file=" + file.getName() +
                ", contentType='" + contentType + '\'' +
                ", contentLength='" + contentLength + '\'' +
                ", xTimeStampedFile='" + xTimeStampedFile + '\'' +
                ", xTriggerType='" + xTriggerType + '\'' +
                ", xEventInfo='" + xEventInfo + '\'' +
                ", host='" + super.getHost() + "\'" +
                "}";
    }
}
