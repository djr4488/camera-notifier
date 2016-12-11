package org.djr.securus.entities;

import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * Created by djr4488 on 12/8/16.
 */
@Entity
@Table(name = "user_logins")
public class UserLogin extends Identifiable {
    @Column(name = "login_datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date loginDate;
    @Column(name = "ip_address")
    private String ipAddress;
    @Column(name = "successful_login")
    private boolean successfulLogin;
    @Column(name = "event")
    private String event;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public UserLogin() {
        this.setCreatedAt(DateTime.now().toDate());
        this.setLastUpdatedAt(DateTime.now().toDate());
    }

    public UserLogin(Date loginDate, String ipAddress, boolean successfulLogin, User user, String event) {
        this();
        this.loginDate = loginDate;
        this.ipAddress = ipAddress;
        this.successfulLogin = successfulLogin;
        this.user = user;
        this.event = event;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public boolean isSuccessfulLogin() {
        return successfulLogin;
    }

    public void setSuccessfulLogin(boolean successfulLogin) {
        this.successfulLogin = successfulLogin;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
