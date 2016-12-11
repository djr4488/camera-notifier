package org.djr.securus.camera.rest.management;

import org.apache.commons.beanutils.BeanUtils;
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
import java.lang.reflect.InvocationTargetException;

/**
 * Created by djr4488 on 12/10/16.
 */
@ApplicationScoped
@Path("user/auth/camera")
public class UserCameraInitiator {
    @Inject
    private Logger log;
    @Inject
    private Event<AddCameraEvent> eventBus;

    @POST
    @Path("add")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response doCameraAdd(@Context HttpServletRequest request, CameraAddRequest cameraAddRequest) {
        log.info("doCameraAdd() cameraAddRequest:{}", cameraAddRequest);
        AddCameraEvent addCameraEvent = new AddCameraEvent();
        try {
            BeanUtils.copyProperties(addCameraEvent, cameraAddRequest);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.debug("doCameraAdd() doesn't support reflection to copy properties... bummer do it the hard way", e);
            addCameraEvent.setCameraName(cameraAddRequest.getCameraName());
            addCameraEvent.setCameraAdministrator(cameraAddRequest.getCameraAdministrator());
            addCameraEvent.setCameraPassword(cameraAddRequest.getCameraPassword());
            addCameraEvent.setProcessNotifyEvents(cameraAddRequest.isProcessNotifyEvents());
            addCameraEvent.setSendNotifyEventAsEmail(cameraAddRequest.isSendNotifyEventAsEmail());
            addCameraEvent.setSendNotifyEventAsSms(cameraAddRequest.isSendNotifyEventAsSms());
            addCameraEvent.setProcessPostEvents(cameraAddRequest.isProcessPostEvents());
            addCameraEvent.setCameraTriggerUrl(cameraAddRequest.getCameraTriggerUrl());
            addCameraEvent.setCameraZone(cameraAddRequest.getCameraZone());
        }
        addCameraEvent.setUserId((Long)request.getSession().getAttribute("userId"));
        eventBus.fire(addCameraEvent);
        return Response.ok().build();
    }
}
