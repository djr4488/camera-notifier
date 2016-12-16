package org.djr.securus.user.rest.password;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by djr4488 on 12/15/16.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class InitForgotPasswordRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    @XmlElement
    private String userName;

    public InitForgotPasswordRequest() {
    }

    public InitForgotPasswordRequest(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
