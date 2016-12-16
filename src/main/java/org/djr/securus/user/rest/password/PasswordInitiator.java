package org.djr.securus.user.rest.password;

import com.codahale.metrics.annotation.Timed;
import org.djr.securus.UserController;
import org.djr.securus.user.UserException;
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
 * Created by djr4488 on 12/14/16.
 */
@ApplicationScoped
@Path("user/auth")
public class PasswordInitiator {
    @Inject
    private Logger log;
    @Inject
    private UserController userController;

    @POST
    @Path("changePassword")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Timed
    public Response doChangePassword(@Context HttpServletRequest request, ChangePasswordRequest changePasswordRequest) {
        log.info("doChangePassword() entered");
        Long userId = (Long)request.getSession().getAttribute("userId");
        boolean success = userController.changePassword(userId, changePasswordRequest.getOldPassword(),
                changePasswordRequest.getNewPassword(), changePasswordRequest.getConfirmPassword(),
                request.getRemoteAddr());
        if (success) {
            return Response.ok().build();
        } else {
            throw new UserException("Failed password change operation");
        }
    }

    @POST
    @Path("initForgotPassword")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Timed
    public Response doInitForgotPassword(InitForgotPasswordRequest initForgotPasswordRequest) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    @POST
    @Path("initForgotPassword")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Timed
    public Response doChangeForgottenPassword(ChangeForgottenPasswordRequest changeForgottenPasswordRequest) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }
}
