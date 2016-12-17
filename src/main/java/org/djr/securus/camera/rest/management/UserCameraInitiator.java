package org.djr.securus.camera.rest.management;

import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
@Api("cameraManagement")
public class UserCameraInitiator {
    @Inject
    private Logger log;
    @Inject
    private Event<CameraManagementEvent> eventBus;

    @POST
    @Path("add")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Timed
    @ApiOperation(value = "doCameraAdd", notes = "Add a new camera")
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

    @DELETE
    @Path("delete")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Timed
    @ApiOperation(value = "doCameraDelete", notes = "Delete a camera.")
    public Response doCameraDelete(@Context HttpServletRequest request, CameraDeleteRequest cameraDeleteRequest) {
        log.info("doCameraDelete() cameraDeleteRequest:{}", cameraDeleteRequest);
        DeleteCameraEvent deleteCameraEvent = new DeleteCameraEvent();
        try {
            BeanUtils.copyProperties(deleteCameraEvent, cameraDeleteRequest);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.debug("doCameraDelete() doesn't support reflection to copy properties... bummer do it the hard way", e);
            deleteCameraEvent.setCameraName(cameraDeleteRequest.getCameraName());
        }
        deleteCameraEvent.setUserId((Long)request.getSession().getAttribute("userId"));
        eventBus.fire(deleteCameraEvent);
        return Response.ok().build();
    }

    @POST
    @Path("update")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Timed
    @ApiOperation(value = "doCameraUpdate", notes = "Update camera management settings")
    public Response doCameraUpdate(@Context HttpServletRequest request, CameraUpdateRequest cameraUpdateRequest) {
        log.info("doCameraUpdate() cameraUpdateRequest:{}", cameraUpdateRequest);
        UpdateCameraEvent updateCameraEvent = new UpdateCameraEvent();
        try {
            BeanUtils.copyProperties(updateCameraEvent, cameraUpdateRequest);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.debug("doCameraUpdate() doesn't support reflection to copy properties... bummer do it the hard way", e);
            updateCameraEvent.setCameraName(cameraUpdateRequest.getCameraName());
            updateCameraEvent.setCameraAdministrator(cameraUpdateRequest.getCameraAdministrator());
            updateCameraEvent.setCameraPassword(cameraUpdateRequest.getCameraPassword());
            updateCameraEvent.setProcessNotifyEvents(cameraUpdateRequest.isProcessNotifyEvents());
            updateCameraEvent.setSendNotifyEventAsEmail(cameraUpdateRequest.isSendNotifyEventAsEmail());
            updateCameraEvent.setSendNotifyEventAsSms(cameraUpdateRequest.isSendNotifyEventAsSms());
            updateCameraEvent.setProcessPostEvents(cameraUpdateRequest.isProcessPostEvents());
            updateCameraEvent.setCameraTriggerUrl(cameraUpdateRequest.getCameraTriggerUrl());
            updateCameraEvent.setCameraZone(cameraUpdateRequest.getCameraZone());
        }
        updateCameraEvent.setUserId((Long)request.getSession().getAttribute("userId"));
        eventBus.fire(updateCameraEvent);
        return Response.ok().build();
    }
}
