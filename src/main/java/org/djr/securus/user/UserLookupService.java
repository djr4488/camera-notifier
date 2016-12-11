package org.djr.securus.user;

import org.djr.securus.entities.User;
import org.djr.securus.entities.UserLogin;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Created by djr4488 on 12/7/16.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class UserLookupService {
    @Inject
    private Logger log;
    @PersistenceContext(unitName = "camera_notifier")
    private EntityManager em;

    public User lookupUserByUserName(String userName) {
        log.debug("lookupUserByUserName() userName:{}", userName);
        try {
            TypedQuery<User> userQuery = em.createNamedQuery("findByUserName", User.class);
            userQuery.setParameter("userName", userName);
            return userQuery.getSingleResult();
        } catch (NoResultException nrEx) {
            return null;
        }
    }

    public User lookUserById(Long id) {
        log.debug("lookUserById() id:{}", id);
        return em.find(User.class, id);
    }

    public User persistNewUser(User user) {
        em.persist(user);
        return user;
    }

    public void addUserLogin(User user, String ipAddress, boolean success, String event) {
        UserLogin userLogin = new UserLogin(DateTime.now().toDate(), ipAddress, success, user, event);
        em.persist(userLogin);
    }
}
