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
    private String userName;
    @XmlElement
    private String passwordRecoveryToken;
    @XmlElement
    private String newPassword;
    @XmlElement
    private String confirmPassword;

    public ChangeForgottenPasswordRequest() {
    }

    public ChangeForgottenPasswordRequest(String userName, String passwordRecoveryToken, String newPassword, String confirmPassword) {
        this.userName = userName;
        this.passwordRecoveryToken = passwordRecoveryToken;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPasswordRecoveryToken() {
        return passwordRecoveryToken;
    }

    public void setPasswordRecoveryToken(String passwordRecoveryToken) {
        this.passwordRecoveryToken = passwordRecoveryToken;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
