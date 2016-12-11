package org.djr.securus;

import org.djr.securus.exceptions.BusinessException;
import org.djr.securus.log.LogEvent;
import org.djr.securus.log.LogType;
import org.djr.securus.user.rest.add.UserExistsException;

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
        if (e instanceof UserExistsException) {
            resp = Response.status(Response.Status.CONFLICT).build();
            logEventBus.fire(new LogEvent(LogType.DEBUG, "toResponse() error occurred", null, e));
        } else {
            resp = Response.status(Response.Status.BAD_REQUEST).build();
            logEventBus.fire(new LogEvent(LogType.DEBUG, "toResponse() error occurred", null, e));
        }
        return resp;
    }
}
