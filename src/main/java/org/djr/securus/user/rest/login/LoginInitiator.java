package org.djr.securus.user.rest.login;

import org.djr.securus.TokenService;
import org.djr.securus.UserController;
import org.djr.securus.entities.User;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
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
public class LoginInitiator {
    @Inject
    private Logger log;
    @Inject
    private Event<LoginRequest> eventBus;
    @Inject
    private UserController userController;
    @Inject
    private TokenService tokenService;

    @POST
    @Path("login")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response doLogin(@Context HttpServletRequest request, LoginRequest loginRequest) {
        log.info("doLogin() entered loginRequest:{}", loginRequest);
        Response resp;
        HttpSession session = request.getSession(false);
        User user;
        if (!isValidSession(session)) {
            user = userController.validateUser(loginRequest.getUserName(), loginRequest.getPassword(),
                    request.getRemoteAddr(), "LOGIN");
            if (null != user) {session = request.getSession();
                resp = Response.ok()
                        .entity(new LoginResponse("/api/user/management", null, null))
                        .build();
                session.setAttribute("token", tokenService.generateToken(user).getToken());
                session.setAttribute("userId", user.getId());
            } else {
                resp = Response.status(Response.Status.UNAUTHORIZED)
                        .entity(new LoginResponse(null, "User or password don't match", "Failed Login"))
                        .build();
            }
        } else {
            // already logged in
            resp = Response.ok()
                    .entity(new LoginResponse("/api/user/management", null, null))
                    .build();
        }
        return resp;
    }

    private boolean isValidSession(HttpSession session) {
        boolean isValidSession = false;
        if (null != session) {
            isValidSession = true;
        }
        return isValidSession;
    }
}
