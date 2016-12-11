package org.djr.securus;

import org.djr.securus.user.PasswordUtils;
import org.djr.securus.entities.Token;
import org.djr.securus.entities.User;
import org.djr.securus.exceptions.SystemException;
import org.djr.securus.user.rest.add.AddUserRequest;
import org.djr.securus.user.rest.add.UserExistsException;
import org.djr.securus.user.rest.login.LoginRequest;
import org.djr.securus.user.UserLookupService;
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

    public User validateUser(String userName, String password, String ipAddress, String event) {
        User user = userLookupService.lookupUserByUserName(userName);
        byte[] userPasswordHash = Base64.base64Decode(user.getPassword().getBytes());
        byte[] userPasswordSalt = Base64.base64Decode(user.getSalt().getBytes());
        char[] providedPasswordHash = password.toCharArray();
        boolean passwordsMatch = PasswordUtils.isExpectedPassword(providedPasswordHash, userPasswordSalt, userPasswordHash);
        userLookupService.addUserLogin(user, ipAddress, passwordsMatch, event);
        if (passwordsMatch) {
            return user;
        } else {
            return null;
        }
    }

    public User findUser(Long id) {
        return userLookupService.lookUserById(id);
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
