package org.djr.securus.camera.rest.management;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * Created by djr4488 on 8/4/17.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class GetCamerasResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    @XmlElement
    private List<CameraInfo> cameras;

    public GetCamerasResponse() {
    }

    public GetCamerasResponse(List<CameraInfo> cameras) {
        this.cameras = cameras;
    }

    public List<CameraInfo> getCameras() {
        return cameras;
    }

    public void setCameras(List<CameraInfo> cameras) {
        this.cameras = cameras;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
