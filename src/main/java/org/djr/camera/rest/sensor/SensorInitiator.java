package org.djr.camera.rest.sensor;

import org.djr.camera.SensorEvent;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

/**
 * Created by djr4488 on 12/7/16.
 */
@ApplicationScoped
@Path("sensor")
public class SensorInitiator {
    @Inject
    private Logger log;
    @Inject
    private Event<SensorEvent> eventBus;

    @Path("trigger")
    public Response doTrigger(@Context HttpServletRequest request) {
        String userName = (String)request.getAttribute("userName");
        String cameraName= (String)request.getAttribute("cameraName");
        log.info("doTrigger() entered userName:{}, cameraName:{}", userName, cameraName);
        eventBus.fire(new SensorEvent(userName, cameraName));
        return Response.ok().build();
    }
}
