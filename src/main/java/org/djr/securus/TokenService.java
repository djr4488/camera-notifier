package org.djr.securus;

import org.djr.securus.entities.Token;
import org.djr.securus.entities.User;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.UUID;

/**
 * Created by djr4488 on 12/10/16.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class TokenService {
    @Inject
    private Logger log;
    @PersistenceContext(unitName = "camera_notifier")
    private EntityManager em;

    public Token generateToken(User user) {
        log.debug("generateToken() entered");
        Token token = new Token(UUID.randomUUID().toString(), DateTime.now().toDate(), user);
        em.persist(token);
        return token;
    }
}
