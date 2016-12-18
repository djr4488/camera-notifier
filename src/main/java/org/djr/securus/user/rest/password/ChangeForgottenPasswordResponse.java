package org.djr.securus.user.rest.password;

import org.djr.securus.user.rest.UserResponse;

import java.io.Serializable;

/**
 * Created by djr4488 on 12/18/16.
 */
public class ChangeForgottenPasswordResponse extends UserResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    public ChangeForgottenPasswordResponse() {
    }

    public ChangeForgottenPasswordResponse(String forwardUrl, String msg, String msgBold) {
        super.setForwardUrl(forwardUrl);
        super.setMsg(msg);
        super.setMsg(msgBold);
    }
}
