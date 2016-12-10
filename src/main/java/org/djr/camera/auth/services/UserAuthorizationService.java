package org.djr.camera.auth.services;

import org.djr.camera.entities.Token;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Startup
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class UserAuthorizationService {
    @Inject
    private Logger log;
    @PersistenceContext(unitName = "camera_notifier")
    private EntityManager em;

    public Token getToken(String token) {
        log.debug("getToken() looking up token:{}", token);
        TypedQuery<Token> tokenQuery = em.createNamedQuery("findByToken", Token.class);
        tokenQuery.setParameter("token", token);
        tokenQuery.setParameter("expirationTime", DateTime.now().minusMinutes(30).toDate());
        return tokenQuery.getSingleResult();
    }

    @Schedule(minute = "*/30", hour = "*")
    public void deleteExpiredTokens() {
        log.info("deleteExpiredTokens()");
        TypedQuery<Token> tokenQuery = em.createNamedQuery("deleteExpiredTokens", Token.class);
        tokenQuery.setParameter("expirationTime", DateTime.now().minusMinutes(30).toDate());
        tokenQuery.executeUpdate();
    }
}
