package org.djr.securus.user.rest.add;

import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
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
@ApplicationScoped
@Path("user")
public class AddUserInitiator {
    @Inject
    private Logger log;
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
