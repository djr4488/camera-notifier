package org.djr.camera.entities;

import org.djr.camera.entities.Identifiable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * Created by djr4488 on 12/6/16.
 */
@Entity
@Table(name = "users",
        indexes = { @Index(name="id_index", columnList = "id"),
                    @Index(name = "user_name_index", unique = true, columnList = "userName")})
@NamedQueries(
        {@NamedQuery(name = "findByUserName",
                     query = "select user from User user where user.userName = :userName")}
)
public class User extends Identifiable {
    private static final long serialVersionUID = 1L;
    @Column(name = "user_name", nullable = false)
    private String userName;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "email_address")
    private String emailAddress;
    @OneToMany
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private List<Camera> cameras;
    @OneToMany
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private List<UserLogin> userLogins;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public List<Camera> getCameras() {
        return cameras;
    }

    public void setCameras(List<Camera> cameras) {
        this.cameras = cameras;
    }
}
