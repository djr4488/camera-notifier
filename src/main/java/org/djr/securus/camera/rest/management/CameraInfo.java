package org.djr.securus.camera.rest.management;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by djr4488 on 8/4/17.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class CameraInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    @XmlElement
    private Long id;
    @XmlElement
    private String cameraName;

    public CameraInfo() {
    }

    public CameraInfo(Long id, String cameraName) {
        this.id = id;
        this.cameraName = cameraName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
