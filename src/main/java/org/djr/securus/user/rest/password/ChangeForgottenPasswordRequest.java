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
public class ChangeForgottenPasswordRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    @XmlElement
    private String forgotPasswordToken;
    @XmlElement
    private String newPassword;
    @XmlElement
    private String oldPassword;

    public ChangeForgottenPasswordRequest() {
    }

    public ChangeForgottenPasswordRequest(String forgotPasswordToken, String newPassword, String oldPassword) {
        this.forgotPasswordToken = forgotPasswordToken;
        this.newPassword = newPassword;
        this.oldPassword = oldPassword;
    }

    public String getForgotPasswordToken() {
        return forgotPasswordToken;
    }

    public void setForgotPasswordToken(String forgotPasswordToken) {
        this.forgotPasswordToken = forgotPasswordToken;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
