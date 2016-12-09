package org.djr.camera.services;

import org.djr.camera.entities.User;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
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

    //TODO perform entity manager named query here for now hard coded to return a "user" with a "camera"
    //TODO full names when coding properly
    public User lookupUserByUserName(String userName) {
        log.debug("lookupUserByUserName() userName:{}", userName);
        TypedQuery<User> userQuery = em.createNamedQuery("findByUserName", User.class);
        userQuery.setParameter("userName", userName);
        return userQuery.getSingleResult();
    }
}
