package org.djr.camera;

import org.djr.camera.auth.utils.PasswordUtils;
import org.djr.camera.entities.User;
import org.djr.camera.exceptions.BusinessException;
import org.djr.camera.exceptions.SystemException;
import org.djr.camera.rest.user.add.AddUserRequest;
import org.djr.camera.services.UserLookupService;
import org.eclipse.persistence.internal.oxm.conversion.Base64;
import org.slf4j.Logger;

import javax.ejb.EJBTransactionRolledbackException;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

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
            user.setEmailAddress(request.getEmailAddress());
            try {
                userLookupService.persistNewUser(user);
            } catch (Exception ex) {
                //TODO specify exact business exception later
                throw new BusinessException("Could not persist user", ex);
            }
        } else {
            throw new BusinessException("Could not persist user, because user already exists.");
        }
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
