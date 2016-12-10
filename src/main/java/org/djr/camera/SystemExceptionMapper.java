package org.djr.camera;

import org.djr.camera.exceptions.SystemException;
import org.djr.camera.log.LogEvent;
import org.djr.camera.log.LogType;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Created by djr4488 on 12/10/16.
 */
@ApplicationScoped
@Provider
public class SystemExceptionMapper implements ExceptionMapper<SystemException> {
    @Inject
    private Event<LogEvent> logEventBus;

    public Response toResponse(SystemException e) {
        Response resp;
        resp = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        logEventBus.fire(new LogEvent(LogType.ERROR, "toResponse() error occurred:", null, e));
        return resp;
    }
}
