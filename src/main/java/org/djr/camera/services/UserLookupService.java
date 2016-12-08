package org.djr.camera.services;

import org.djr.camera.entities.Camera;
import org.djr.camera.entities.User;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by djr4488 on 12/7/16.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class UserLookupService {
    @Inject
    private Logger log;
    @PersistenceContext(unitName = "camera_database")
    private EntityManager em;

    //TODO perform entity manager named query here for now hard coded to return a "user" with a "camera"
    //TODO full names when coding properly
    public User lookupUserByUserName(String userName) {
        User u = new User();
        List<Camera> cameras = new ArrayList<Camera>();
        Camera c = new Camera();
        c.setCameraName("cameraName");
        c.setCameraUserName("administrator");
        c.setCameraPassword("testPassword");
        c.setCameraUrl("http://192.168.1.183");
        cameras.add(c);
        u.setCameras(cameras);
        return u;
    }
}
