package org.djr.securus.user.rest.password;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by djr4488 on 12/14/16.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class ChangePasswordRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    @XmlElement
    private String oldPassword;
    @XmlElement
    private String newPassword;
    @XmlElement
    private String confirmPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
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
}
