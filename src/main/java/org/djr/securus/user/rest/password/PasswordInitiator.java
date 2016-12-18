package org.djr.securus.user.rest.password;

import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Path("user")
@Api("passwordManagement")
public class PasswordInitiator {
    @Inject
    private Logger log;
    @Inject
    private UserController userController;

    @POST
    @Path("auth/changePassword")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Timed
    @ApiOperation(value = "doChangePassword", notes = "Change user password")
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
    @ApiOperation(value = "doInitForgetPassword", notes = "Initiate change forgotten user password")
    public Response doInitForgotPassword(@Context HttpServletRequest request, InitForgotPasswordRequest initForgotPasswordRequest) {
        userController.passwordRecovery(initForgotPasswordRequest, request.getRemoteAddr());
        return Response.ok().entity(new InitPasswordRecoveryResponse("doPasswordRecovery", null, null)).build();
    }

    @POST
    @Path("changeForgottenPassword")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Timed
    @ApiOperation(value = "doChangeForgottenPassword", notes = "Change forgotten password")
    public Response doChangeForgottenPassword(@Context HttpServletRequest request, ChangeForgottenPasswordRequest changeForgottenPasswordRequest) {
        userController.recoverPassword(changeForgottenPasswordRequest, request.getRemoteAddr());
        return Response.ok().entity(new ChangeForgottenPasswordResponse("doLogin", null, null)).build();
    }
}
