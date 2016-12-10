package org.djr.camera.entities;

import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "tokens",
       indexes = {@Index(name = "tokens_id_index", columnList = "id"),
                  @Index(name = "tokens_token_index", columnList = "token"),
                  @Index(name = "tokens_token_expiration_index", columnList = "tokenExpiration")})
@NamedQueries({
        @NamedQuery(name = "findByToken",
                    query = "select token from Token token where token.token = :token and token.tokenExpiration >= :expirationTime"),
        @NamedQuery(name = "deleteExpiredTokens",
                    query = "delete from Token token where token.tokenExpiration < :expirationTime")
})
public class Token extends Identifiable {
    @Column(name = "token")
    private String token;
    @Column(name = "token_expiration")
    @Temporal(TemporalType.TIMESTAMP)
    private Date tokenExpiration;
    @OneToOne
    @JoinColumn()
    private User user;

    public Token() {
        DateTime now = DateTime.now();
        this.setCreatedAt(now.toDate());
        this.setLastUpdatedAt(now.toDate());
    }

    public Token(String token, Date tokenExpiration) {
        this();
        this.token = token;
        this.tokenExpiration = tokenExpiration;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getTokenExpiration() {
        return tokenExpiration;
    }

    public void setTokenExpiration(Date tokenExpiration) {
        this.tokenExpiration = tokenExpiration;
    }
}
