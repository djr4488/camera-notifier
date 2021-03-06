package org.djr.securus.user.rest.login;

import com.codahale.metrics.annotation.Timed;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.djr.securus.TokenService;
import org.djr.securus.UserController;
import org.djr.securus.entities.User;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("user")
@Api("login")
public class LoginInitiator {
    @Inject
    private Logger log;
    @Inject
    private UserController userController;
    @Inject
    private TokenService tokenService;

    @POST
    @Path("login")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Timed
    @ApiOperation(value = "doLogin", notes = "Login user")
    public Response doLogin(@Context HttpServletRequest request, LoginRequest loginRequest) {
        log.info("doLogin() entered loginRequest:{}", loginRequest);
        Response resp;
        HttpSession session = request.getSession(false);
        User user;
        if (!isValidSession(session)) {
            log.debug("doLogin() no valid session detected");
            user = userController.validateUser(loginRequest.getUserName(), loginRequest.getPassword(),
                    request.getRemoteAddr(), "LOGIN");
            if (null != user) {
                log.debug("doLogin() valid user found");
                session = request.getSession();
                resp = Response.ok()
                        .entity(new LoginResponse("/api/user/management", null, null))
                        .build();
                session.setAttribute("token", tokenService.generateToken(user).getToken());
                session.setAttribute("userId", user.getId());
            } else {
                log.debug("doLogin() no valid user found");
                resp = Response.status(Response.Status.UNAUTHORIZED)
                        .entity(new LoginResponse(null, "User or password don't match", "Failed Login"))
                        .build();
            }
        } else {
            log.debug("doLogin() already logged in");
            // already logged in
            resp = Response.ok()
                    .entity(new LoginResponse("/api/user/management", null, null))
                    .build();
        }
        return resp;
    }

    private boolean isValidSession(HttpSession session) {
        boolean isValidSession = false;
        if (null != session && null != session.getAttribute("token")) {
            isValidSession = true;
        }
        return isValidSession;
    }
}
