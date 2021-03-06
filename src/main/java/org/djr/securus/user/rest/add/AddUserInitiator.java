package org.djr.securus.user.rest.add;

import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api("user")
public class AddUserInitiator {
    @Inject
    private Logger log;
    @Inject
    private Event<AddUserRequest> eventBus;

    @POST
    @Path("add")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Timed
    @ApiOperation(value = "doAddUser", notes = "Add a user")
    public Response doAddUser(@Context HttpServletRequest request, AddUserRequest addUserRequest) {
        log.info("addUser() addUserRequest:{}", addUserRequest);
        eventBus.fire(addUserRequest);
        return Response.ok().build();
    }
}
