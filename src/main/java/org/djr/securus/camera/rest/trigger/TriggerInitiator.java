package org.djr.securus.camera.rest.trigger;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by djr4488 on 12/7/16.
 */
@ApplicationScoped
@Path("camera")
public class TriggerInitiator {
    @Inject
    private Logger log;
    @Inject
    private Event<TriggerEvent> eventBus;

    @GET
    @Path("trigger")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Timed
    public Response doTrigger(@Context HttpServletRequest request, TriggerRequest triggerRequest) {
        log.info("doTrigger() entered triggerRequest:{}", triggerRequest);
        eventBus.fire(new TriggerEvent((Long)request.getAttribute("user_id"), triggerRequest.getZoneName()));
        return Response.ok().build();
    }
}
