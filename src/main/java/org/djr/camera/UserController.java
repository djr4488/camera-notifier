package org.djr.camera;

import org.djr.camera.auth.utils.PasswordUtils;
import org.djr.camera.entities.Token;
import org.djr.camera.entities.User;
import org.djr.camera.entities.UserLogin;
import org.djr.camera.exceptions.SystemException;
import org.djr.camera.rest.user.add.AddUserRequest;
import org.djr.camera.rest.user.add.UserExistsException;
import org.djr.camera.rest.user.login.LoginRequest;
import org.djr.camera.services.UserLookupService;
import org.eclipse.persistence.internal.oxm.conversion.Base64;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.ejb.EJBTransactionRolledbackException;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.UUID;

/**
 * Created by djr4488 on 12/10/16.
 */
@ApplicationScoped
public class UserController {
    @Inject
    private Logger log;
    @Inject
    private UserLookupService userLookupService;
    public void addUser(@Observes AddUserRequest request) {
        if (!isExistingUser(request.getUserName())) {
            User user = new User();
            user.setUserName(request.getUserName());
            byte[] nextSalt = PasswordUtils.getNextSalt();
            char[] password = request.getPassword().toCharArray();
            String base64Hash = new String(Base64.base64Encode(PasswordUtils.hash(password, nextSalt)));
            user.setPassword(base64Hash);
            user.setSalt(new String(Base64.base64Encode(nextSalt)));
            user.setEmailAddress(request.getEmailAddress());
            userLookupService.persistNewUser(user);
        } else {
            throw new UserExistsException("Could not persist user, because user already exists.");
        }
    }

    public User validateUser(LoginRequest loginRequest, String ipAddress) {
        User user = userLookupService.lookupUserByUserName(loginRequest.getUserName());
        byte[] userPasswordHash = Base64.base64Decode(user.getPassword().getBytes());
        byte[] userPasswordSalt = Base64.base64Decode(user.getSalt().getBytes());
        char[] providedPasswordHash = loginRequest.getPassword().toCharArray();
        boolean passwordsMatch = PasswordUtils.isExpectedPassword(providedPasswordHash, userPasswordSalt, userPasswordHash);
        userLookupService.addUserLogin(user, ipAddress, passwordsMatch, "LOGIN");
        if (passwordsMatch) {
            return user;
        } else {
            return null;
        }
    }

    public Token generateToken() {
        Token token = new Token(UUID.randomUUID().toString(), DateTime.now().toDate());
        return token;
    }

    private boolean isExistingUser(String userName) {
        try {
            return null != userLookupService.lookupUserByUserName(userName);
        } catch (EJBTransactionRolledbackException ejbTxRbEx) {
            throw new SystemException("Unexpected ejb exception occurred", ejbTxRbEx);
        } catch (Exception ex) {
            throw new SystemException("Unexpected non-ejb exception occurred", ex);
        }
    }
}
