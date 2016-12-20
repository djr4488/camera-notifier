package org.djr.securus;

import org.djr.securus.camera.CameraEventService;
import org.djr.securus.camera.rest.management.AddCameraEvent;
import org.djr.securus.camera.rest.management.DeleteCameraEvent;
import org.djr.securus.camera.rest.management.UpdateCameraEvent;
import org.djr.securus.entities.UserLogin;
import org.djr.securus.exceptions.BusinessException;
import org.djr.securus.messaging.email.EmailService;
import org.djr.securus.user.PasswordUtils;
import org.djr.securus.entities.User;
import org.djr.securus.exceptions.SystemException;
import org.djr.securus.user.UserException;
import org.djr.securus.user.UserPasswordMismatchException;
import org.djr.securus.user.rest.add.AddUserRequest;
import org.djr.securus.user.rest.add.UserExistsException;
import org.djr.securus.user.UserLookupService;
import org.djr.securus.user.rest.password.ChangeForgottenPasswordRequest;
import org.djr.securus.user.rest.password.InitForgotPasswordRequest;
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
    @Inject
    private EmailService emailService;

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

    public void passwordRecovery(InitForgotPasswordRequest request, String ipAddress) {
        try {
            log.debug("passwordRecoveryGenerator() request:{}", request);
            User user = userLookupService.lookupUserByUserName(request.getUserName());
            if (null != user) {
                String passwordRecoveryString = PasswordUtils.generateRandomPassword(10);
                user.setPasswordRecovery(passwordRecoveryString);
                user.setLastUpdatedAt(DateTime.now().toDate());
                StringBuilder sb = new StringBuilder("Below is your password change key.  If you didn't request changing your password");
                sb.append(" then you may ignore this email.  \n\n").append(passwordRecoveryString);
                emailService.sendEmail("Password Change Request", user.getEmailAddress(), sb.toString());
                userLookupService.updateUser(user, ipAddress, true, "Init Password Recovery");
            }
        } catch (Exception ex) {
            log.error("Failed password recovery", ex);
            throw new BusinessException(ex);
        }
    }

    public void recoverPassword(ChangeForgottenPasswordRequest request, String ipAddress) {
        try {
            log.debug("recoverPassword() ipAddress:{}, userName:{}", ipAddress, request.getUserName());
            User user = userLookupService.lookupUserByUserName(request.getUserName());
            if (isRecoveryKeyValid(request.getPasswordRecoveryToken(), user)) {
                passwordMatchCheck(request.getNewPassword(), request.getConfirmPassword());
                user.setLastUpdatedAt(DateTime.now().toDate());
                setUserPassword(request.getNewPassword(), user);
                StringBuilder sb = new StringBuilder("Your password was changed.\n\n  If this wasn't you, I'd change passwords on both your");
                sb.append(" email and home security.");
                emailService.sendEmail("Home Security Password Changed", user.getEmailAddress(), sb.toString());
                user.setPasswordRecovery(null);
                userLookupService.updateUser(user, ipAddress, true, "Password Recovery");
            } else {
                throw new BusinessException("User not found or invalid recovery token");
            }
        } catch (Exception ex) {
            log.error("recoverPassword() ", ex);
            throw new BusinessException(ex);
        }
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

    private boolean isRecoveryKeyValid(String recoveryKey, User user) {
        boolean validRecoveryKey = false;
        if (null != user && null != user.getPasswordRecovery() && user.getPasswordRecovery().equals(recoveryKey)) {
            validRecoveryKey = true;
        }
        return validRecoveryKey;
    }
}
