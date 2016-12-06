package org.djr.camera.rest.post;

import com.codahale.metrics.annotation.Timed;
import org.djr.camera.CameraPostEvent;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.File;

/**
 * Created by djr4488 on 12/4/16.
 */
@ApplicationScoped
@Path("/camera")
public class CameraPostInitiator {
    @Inject
    private Logger log;
    @Inject
    private Event<CameraPostEvent> eventBus;

    @Path("/post/{camera}")
    @POST
    @Consumes({"*/*", "video/x-msvideo"})
    @Timed
    public Response doCameraPost(@Context HttpServletRequest request, @PathParam(value="camera") String camera) {
        log.debug("doCameraPost() entered camera:{}", camera);
        CameraPostEvent cameraPostEvent = new CameraPostEvent(camera, request.getHeader("host"),
                request.getHeader("authorization"), (String)request.getAttribute("userName"), (String)request.getAttribute("password"),
                (File)request.getAttribute("file"), request.getHeader("content-type"),
                request.getHeader("content-length"), request.getHeader("x-timestampedfile"), request.getHeader("x-triggertype"),
                request.getHeader("x-eventinfo"));
        eventBus.fire(cameraPostEvent);
        return Response.ok().build();
    }
}
