package org.djr.securus.user.rest;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by djr4488 on 12/18/16.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public abstract class UserResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    @XmlElement
    private String forwardUrl;
    @XmlElement
    private String msg;
    @XmlElement
    private String msgBold;

    public UserResponse() {
    }

    public UserResponse(String forwardUrl, String msg, String msgBold) {
        this.forwardUrl = forwardUrl;
        this.msg = msg;
        this.msgBold = msgBold;
    }

    public String getForwardUrl() {
        return forwardUrl;
    }

    public void setForwardUrl(String forwardUrl) {
        this.forwardUrl = forwardUrl;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsgBold() {
        return msgBold;
    }

    public void setMsgBold(String msgBold) {
        this.msgBold = msgBold;
    }
}
