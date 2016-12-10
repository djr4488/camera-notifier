package org.djr.camera;

import org.djr.camera.exceptions.BusinessException;
import org.djr.camera.exceptions.SystemException;
import org.djr.camera.log.LogEvent;
import org.djr.camera.log.LogType;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Created by djr4488 on 12/4/16.
 */
@ApplicationScoped
public class CameraRestExceptionMapper implements ExceptionMapper<Exception>  {
    @Inject
    private Event<LogEvent> logEventBus;

    @Override
    public Response toResponse(Exception e) {
        Response resp;
        if (e instanceof WebApplicationException || e instanceof SystemException) {
            resp = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            logEventBus.fire(new LogEvent(LogType.ERROR, "toResponse() error occurred {}", e));
        } else if (e instanceof BusinessException) {
            resp = Response.status(Response.Status.BAD_REQUEST).build();
            logEventBus.fire(new LogEvent(LogType.DEBUG, "toResponse() error occurred {}", e));
        } else {
            resp = Response.status(Response.Status.BAD_REQUEST).build();
            logEventBus.fire(new LogEvent(LogType.ERROR, "toResponse() error occurred {}", e));
        }
        return resp;
    }
}
