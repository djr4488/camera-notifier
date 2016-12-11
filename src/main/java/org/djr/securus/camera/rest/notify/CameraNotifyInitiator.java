package org.djr.securus.camera.rest.notify;

import com.codahale.metrics.annotation.Timed;
import org.djr.securus.CameraNotifyEvent;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by djr4488 on 11/22/16.
 */
@ApplicationScoped
@Path("/camera")
public class CameraNotifyInitiator {
    @Inject
    private Logger log;
    @Inject
    private Event<CameraNotifyEvent> eventBus;

    @Path("/notify/{camera}")
    @POST
    @Consumes({MediaType.TEXT_HTML, MediaType.APPLICATION_XHTML_XML, "image/webp", MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON})
    @Timed
    public Response doCameraNotify(@Context HttpServletRequest request, @PathParam(value = "camera") String camera) {
        log.info("doCameraNotify() entered securus:{}", camera);
        CameraNotifyEvent cameraNotifyEvent = new CameraNotifyEvent(camera, request.getRemoteHost(),
                request.getHeader("authorization"), (String)request.getAttribute("userName"),
                (String)request.getAttribute("password"));
        eventBus.fire(cameraNotifyEvent);
        return Response.ok().build();
    }
}
