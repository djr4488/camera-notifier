package org.djr.securus;

import org.djr.securus.camera.CameraEventService;
import org.djr.securus.camera.rest.management.AddCameraEvent;
import org.djr.securus.camera.rest.management.DeleteCameraEvent;
import org.djr.securus.camera.rest.management.UpdateCameraEvent;
import org.djr.securus.user.PasswordUtils;
import org.djr.securus.entities.User;
import org.djr.securus.exceptions.SystemException;
import org.djr.securus.user.UserException;
import org.djr.securus.user.UserPasswordMismatchException;
import org.djr.securus.user.rest.add.AddUserRequest;
import org.djr.securus.user.rest.add.UserExistsException;
import org.djr.securus.user.UserLookupService;
import org.eclipse.persistence.internal.oxm.conversion.Base64;
import org.joda.time.DateTime;
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
    @Inject
    private CameraEventService cameraEventService;

    public void addUser(@Observes AddUserRequest request) {
        passwordMatchCheck(request.getPassword(), request.getConfirmPassword());
        if (!isExistingUser(request.getUserName())) {
            User user = new User();
            user.setUserName(request.getUserName());
            setUserPassword(request.getPassword(), user);
            user.setEmailAddress(request.getEmailAddress());
            userLookupService.persistNewUser(user);
        } else {
            throw new UserExistsException("Could not persist user, because user already exists.");
        }
    }

    public User validateUser(String userName, String password, String ipAddress, String event) {
        User user = userLookupService.lookupUserByUserName(userName);
        boolean passwordsMatch = isValidPasswordForUser(password, user);
        userLookupService.addUserLogin(user, ipAddress, passwordsMatch, event);
        if (passwordsMatch) {
            return user;
        } else {
            return null;
        }
    }

    public boolean changePassword(Long userId, String oldPassword, String newPassword, String confirmPassword, String ipAddress) {
        log.debug("changePassword() entered userId:{}, ipAddress:{}", userId, ipAddress);
        boolean changedPassword = false;
        passwordMatchCheck(newPassword, confirmPassword);
        User user = userLookupService.lookUserById(userId);
        if (null != user && isValidPasswordForUser(oldPassword, user)) {
            user.setLastUpdatedAt(DateTime.now().toDate());
            setUserPassword(newPassword, user);
            userLookupService.updateUser(user, ipAddress, true, "Change Password");
            changedPassword = true;
        } else {
            if (null != user) {
                userLookupService.addUserLogin(user, ipAddress, false, "Change Password");
            } else {
                throw new UserException("User not found or wrong password");
            }
        }
        return changedPassword;
    }

    public User findUser(Long id) {
        return userLookupService.lookUserById(id);
    }

    public void addCameraListener(@Observes AddCameraEvent addCameraEvent) {
        log.debug("addCameraListener() addCameraEvent:{}", addCameraEvent);
        User user = findUser(addCameraEvent.getUserId());
        if (null != user) {
            cameraEventService.addCamera(addCameraEvent, user);
        }
    }

    public void deleteCameraListener(@Observes DeleteCameraEvent deleteCameraEvent) {
        log.debug("deleteCameraListener() deleteCameraEvent:{}", deleteCameraEvent);
        User user = findUser(deleteCameraEvent.getUserId());
        if (null != user) {
            cameraEventService.deleteCamera(deleteCameraEvent, user);
        }
    }

    private void passwordMatchCheck(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new UserPasswordMismatchException("Passwords did not match");
        }
    }

    public void updateCameraListener(@Observes UpdateCameraEvent updateCameraEvent) {
        log.debug("updateCameraListener() updateCameraEvent:{}", updateCameraEvent);
        User user = findUser(updateCameraEvent.getUserId());
        if (null != user) {
            cameraEventService.updateCamera(updateCameraEvent, user);
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

    private boolean isValidPasswordForUser(String password, User user) {
        byte[] userPasswordHash = Base64.base64Decode(user.getPassword().getBytes());
        byte[] userPasswordSalt = Base64.base64Decode(user.getSalt().getBytes());
        char[] providedPasswordHash = password.toCharArray();
        return PasswordUtils.isExpectedPassword(providedPasswordHash, userPasswordSalt, userPasswordHash);
    }

    private void setUserPassword(String userPassword, User user) {
        byte[] nextSalt = PasswordUtils.getNextSalt();
        char[] password = userPassword.toCharArray();
        String base64Hash = new String(Base64.base64Encode(PasswordUtils.hash(password, nextSalt)));
        user.setPassword(base64Hash);
        user.setSalt(new String(Base64.base64Encode(nextSalt)));
    }
}
