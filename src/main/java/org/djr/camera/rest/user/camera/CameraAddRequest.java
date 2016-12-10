package org.djr.camera.rest.user.camera;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by djr4488 on 12/10/16.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class CameraAddRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    @XmlElement
    private String cameraName;

    public CameraAddRequest() {
    }

    public CameraAddRequest(String cameraName) {
        this.cameraName = cameraName;
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }
}
