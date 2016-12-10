package org.djr.camera.rest.user.add;

import org.djr.camera.entities.User;
import org.djr.camera.rest.user.add.AddUserRequest;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by djr4488 on 12/8/16.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Path("user")
public class AddUserInitiator {
    @Inject
    private Logger log;
    @PersistenceContext(unitName = "camera_notifier")
    private EntityManager em;
    @Inject
    private Event<AddUserRequest> eventBus;

    @POST
    @Path("add")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response addUser(@Context HttpServletRequest request, AddUserRequest addUserRequest) {
        log.info("addUser() addUserRequest:{}", addUserRequest);
        eventBus.fire(addUserRequest);
        return Response.ok().build();
    }
}
