package org.djr.camera;

import org.djr.camera.exceptions.BusinessException;
import org.djr.camera.log.LogEvent;
import org.djr.camera.log.LogType;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by djr4488 on 12/4/16.
 */
@ApplicationScoped
@Provider
public class BusinessExceptionMapper implements ExceptionMapper<BusinessException>  {
    @Inject
    private Event<LogEvent> logEventBus;

    @Override
    public Response toResponse(BusinessException e) {
        Response resp;
        resp = Response.status(Response.Status.BAD_REQUEST).build();
        logEventBus.fire(new LogEvent(LogType.DEBUG, "toResponse() error occurred", null, e));
        return resp;
    }
}
