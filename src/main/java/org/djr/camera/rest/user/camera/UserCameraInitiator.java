package org.djr.camera.rest.user.camera;

import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
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
 * Created by djr4488 on 12/10/16.
 */
@ApplicationScoped
@Path("user/auth/camera")
public class UserCameraInitiator {
    @Inject
    private Logger log;

    @POST
    @Path("add")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response doCameraAdd(@Context HttpServletRequest request, CameraAddRequest cameraAddRequest) {
        log.info("doCameraAdd() cameraAddRequest:{}", cameraAddRequest);
        return Response.ok().build();
    }
}
