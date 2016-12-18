package org.djr.securus.user.rest.password;

import org.djr.securus.user.rest.UserResponse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by djr4488 on 12/18/16.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class InitPasswordRecoveryResponse extends UserResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    public InitPasswordRecoveryResponse() {
    }

    public InitPasswordRecoveryResponse(String forwardUrl, String msg, String msgBold) {
        super.setForwardUrl(forwardUrl);
        super.setMsg(msg);
        super.setMsg(msgBold);
    }
}

