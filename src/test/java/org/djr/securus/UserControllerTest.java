package org.djr.securus;

import junit.framework.TestCase;
import org.apache.commons.codec.binary.Base64;
import org.djr.securus.camera.CameraEventService;
import org.djr.securus.camera.rest.management.AddCameraEvent;
import org.djr.securus.camera.rest.management.DeleteCameraEvent;
import org.djr.securus.camera.rest.management.UpdateCameraEvent;
import org.djr.securus.cdi.logs.LoggerProducer;
import org.djr.securus.entities.User;
import org.djr.securus.exceptions.BusinessException;
import org.djr.securus.exceptions.SystemException;
import org.djr.securus.messaging.email.EmailService;
import org.djr.securus.user.PasswordUtils;
import org.djr.securus.user.UserException;
import org.djr.securus.user.UserLookupService;
import org.djr.securus.user.UserPasswordMismatchException;
import org.djr.securus.user.rest.add.AddUserRequest;
import org.djr.securus.user.rest.add.UserExistsException;
import org.djr.securus.user.rest.password.ChangeForgottenPasswordRequest;
import org.djr.securus.user.rest.password.InitForgotPasswordRequest;
import org.jglue.cdiunit.AdditionalClasses;
import org.jglue.cdiunit.CdiRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import javax.ejb.EJBTransactionRolledbackException;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;

/**
 * Created by djr4488 on 12/18/16.
 */
@RunWith(CdiRunner.class)
@AdditionalClasses({LoggerProducer.class})
public class UserControllerTest extends TestCase {
    @Produces
    @Mock
    private UserLookupService userLookupService;
    @Produces
    @Mock
    private CameraEventService cameraEventService;
    @Produces
    @Mock
    private EmailService emailService;
    @Inject
    private Logger log;
    @Inject
    private UserController userController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    //given: AddUserRequest
    //  and: has username test
    //  and: has password test
    //  and: has confirmPassword test
    //  and: has email address test@test.com
    //  and: no prior user exists with username
    // when: adding a new user
    // then: expect no exceptions to occur
    @Test
    public void testAddUserNoPriorUser() {
        AddUserRequest request = new AddUserRequest("test", "test", "test", "test@test.com");
        when(userLookupService.lookupUserByUserName("test")).thenReturn(null);
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        userController.addUser(request);
        verify(userLookupService, times(1)).persistNewUser(userArgumentCaptor.capture());
        User user = userArgumentCaptor.getValue();
        assertNotNull(user);
        assertEquals("test", user.getUserName());
        assertEquals("test@test.com", user.getEmailAddress());
    }

    //given: AddUserRequest
    //  and: has username test
    //  and: has password test
    //  and: has confirmPassword test1
    //  and: has email address test@test.com
    //  and: no prior user exists with username
    // when: adding a new user
    // then: expect UserPasswordMismatchException
    @Test(expected = UserPasswordMismatchException.class)
    public void testAddUserMismatchPassword() {
        AddUserRequest request = new AddUserRequest("test", "test", "test1", "test@test.com");
        when(userLookupService.lookupUserByUserName("test")).thenReturn(null);
        userController.addUser(request);
        verify(userLookupService, never()).persistNewUser(any(User.class));
    }

    //given: AddUserRequest
    //  and: has username test
    //  and: has password test
    //  and: has confirmPassword test
    //  and: has email address test@test.com
    //  and: a prior user exists with username
    // when: adding a new user
    // then: expect UserExistsException
    @Test(expected = UserExistsException.class)
    public void testAddUserExistingUser() {
        AddUserRequest request = new AddUserRequest("test", "test", "test", "test@test.com");
        when(userLookupService.lookupUserByUserName("test")).thenReturn(new User());
        userController.addUser(request);
        verify(userLookupService, never()).persistNewUser(any(User.class));
    }

    //given: AddUserRequest
    //  and: has username test
    //  and: has password test
    //  and: has confirmPassword test
    //  and: has email address test@test.com
    //  and: a prior user exists with username
    //  and: a database transaction rolled back exception happens
    // when: adding a new user
    // then: expect SystemException
    @Test(expected = SystemException.class)
    public void testAddUserWhenEJBTransactionRolledbackException() {
        AddUserRequest request = new AddUserRequest("test", "test", "test", "test@test.com");
        when(userLookupService.lookupUserByUserName("test")).thenThrow(new EJBTransactionRolledbackException());
        userController.addUser(request);
        verify(userLookupService, never()).persistNewUser(any(User.class));
    }

    //given: AddUserRequest
    //  and: has username test
    //  and: has password test
    //  and: has confirmPassword test
    //  and: has email address test@test.com
    //  and: a prior user exists with username
    //  and: unexpected exception is caught
    // when: adding a new user
    // then: expect SystemException
    @Test(expected = SystemException.class)
    public void testAddUserWhenException() {
        AddUserRequest request = new AddUserRequest("test", "test", "test", "test@test.com");
        when(userLookupService.lookupUserByUserName("test")).thenThrow(new SystemException());
        userController.addUser(request);
        verify(userLookupService, never()).persistNewUser(any(User.class));
    }

    //given: a username test
    //  and: the a user is found with username
    //  and: the password provided matches password on user
    // when: validating the user
    // then: expect a user to be returned
    //  and: a login request to be written with true
    @Test
    public void testValidateUserExistsCorrectPassword() {
        User user = getUser();
        when(userLookupService.lookupUserByUserName("test")).thenReturn(user);
        User retUser = userController.validateUser("test", "test", "0.0.0.0", "LoginTest");
        verify(userLookupService, times(1)).addUserLogin(user, "0.0.0.0", true, "LoginTest");
        assertNotNull(retUser);
        assertEquals(user.getPassword(), retUser.getPassword());
    }

    //given: a username test
    //  and: the a user is found with username
    //  and: the password provided does not match password on user
    // when: validating the user
    // then: expect a user to be returned
    //  and: a login request to be written with false
    @Test
    public void testValidateUserExistsIncorrectPassword() {
        User user = getUser();
        when(userLookupService.lookupUserByUserName("test")).thenReturn(user);
        User retUser = userController.validateUser("test", "test1", "0.0.0.0", "LoginTest");
        verify(userLookupService, times(1)).addUserLogin(user, "0.0.0.0", false, "LoginTest");
        assertNull(retUser);
    }

    //given: user id 1
    //  and: user found
    //  and: password old matchings user password
    // when: changing the password
    // then: expect user audit to be written indicating success for change password
    @Test
    public void testChangePasswordSuccess() {
        User user = getUser();
        when(userLookupService.lookUserById(1L)).thenReturn(user);
        userController.changePassword(1L, "test", "test2", "test2", "0.0.0.0");
        verify(userLookupService, times(1)).updateUser(user, "0.0.0.0", true, "Change Password");
    }

    //given: user id 1
    //  and: user found
    //  and: password new does not match confirmation password
    // when: changing the password
    // then: expect no audit to be written
    @Test(expected = UserPasswordMismatchException.class)
    public void testChangePasswordMismatch() {
        User user = getUser();
        when(userLookupService.lookUserById(1L)).thenReturn(user);
        userController.changePassword(1L, "test", "test2", "test3", "0.0.0.0");
        verify(userLookupService, never()).updateUser(user, "0.0.0.0", false, "Change Password");
    }

    //given: user id 1
    //  and: user found
    //  and: password old does not match new
    // when: changing the password
    // then: expect audit to reflect failure
    @Test
    public void testChangePasswordOldDoesNotMatchUser() {
        User user = getUser();
        when(userLookupService.lookUserById(1L)).thenReturn(user);
        userController.changePassword(1L, "test1", "test2", "test2", "0.0.0.0");
        verify(userLookupService, times(1)).addUserLogin(user, "0.0.0.0", false, "Change Password");
    }

    //given: user id 1
    //  and: user not found
    // when: changing password
    // then: expect UserException
    @Test(expected = UserException.class)
    public void testChangePasswordNoUserFound() {
        when(userLookupService.lookUserById(1L)).thenReturn(null);
        userController.changePassword(1L, "test1", "test2", "test2", "0.0.0.0");
        verify(userLookupService, never()).updateUser(any(User.class), anyString(), anyBoolean(), anyString());
        verify(userLookupService, times(1)).addUserLogin(any(User.class), anyString(), anyBoolean(), anyString());
    }

    //given: forgot password request
    //  and: username finds a user
    // when: beginning password recovery process
    // then: expect email sent
    //  and: user audit stored in database
    @Test
    public void testPasswordRecoverySuccess() {
        InitForgotPasswordRequest request = new InitForgotPasswordRequest("test");
        User user = getUser();
        when(userLookupService.lookupUserByUserName("test")).thenReturn(user);
        userController.passwordRecovery(request, "0.0.0.0");
        verify(emailService, times(1)).sendEmail(anyString(), anyString(), anyString());
        verify(userLookupService, times(1)).updateUser(user, "0.0.0.0", true, "Init Password Recovery");
    }

    //given: forgot password request
    //  and: username finds no user
    // when: beginning password recovery process
    // then: expect nothing to happen at all
    @Test
    public void testPasswordRecoveryNoUserFound() {
        InitForgotPasswordRequest request = new InitForgotPasswordRequest("test");
        User user = null;
        when(userLookupService.lookupUserByUserName("test")).thenReturn(user);
        userController.passwordRecovery(request, "0.0.0.0");
        verify(emailService, never()).sendEmail(anyString(), anyString(), anyString());
        verify(userLookupService, never()).updateUser(any(User.class), anyString(), anyBoolean(), anyString());
    }

    //given: forgot password request
    //  and: username finds a user
    // when: beginning password recovery process
    // then: expect email failure
    @Test(expected = BusinessException.class)
    public void testPasswordRecoveryBusinessException() {
        InitForgotPasswordRequest request = new InitForgotPasswordRequest("test");
        User user = getUser();
        when(userLookupService.lookupUserByUserName("test")).thenReturn(user);
        doThrow(new BusinessException("test")).when(emailService).sendEmail(anyString(), anyString(), anyString());
        userController.passwordRecovery(request, "0.0.0.0");
        verify(emailService, times(1)).sendEmail(anyString(), anyString(), anyString());
        verify(userLookupService, never()).updateUser(user, "0.0.0.0", true, "Init Password Recovery");
    }

    //given: change forgotten password request
    //  and: user is found
    //  and: new password matching confirm password
    //  and: recovery token matches recovery token on user
    // when: recovering password
    // then: expect email sent
    //  and: user audit log stored
    @Test
    public void testRecoverPasswordSuccess() {
        ChangeForgottenPasswordRequest request = new ChangeForgottenPasswordRequest("test", "123", "test1", "test1");
        User user = getUser();
        when(userLookupService.lookupUserByUserName("test")).thenReturn(user);
        userController.recoverPassword(request, "0.0.0.0");
        verify(emailService, times(1)).sendEmail(anyString(), anyString(), anyString());
        verify(userLookupService, times(1)).updateUser(user, "0.0.0.0", true, "Password Recovery");
    }

    //given: change forgotten password request
    //  and: user is found
    //  and: new password not matching confirm password
    //  and: recovery token matches recovery token on user
    // when: recovering password
    // then: password mismatch exception
    @Test(expected = BusinessException.class)
    public void testRecoverPasswordMismatch() {
        ChangeForgottenPasswordRequest request = new ChangeForgottenPasswordRequest("test", "123", "test1", "test2");
        User user = getUser();
        when(userLookupService.lookupUserByUserName("test")).thenReturn(user);
        userController.recoverPassword(request, "0.0.0.0");
        verify(emailService, never()).sendEmail(anyString(), anyString(), anyString());
        verify(userLookupService, never()).updateUser(user, "0.0.0.0", true, "Password Recovery");
    }

    //given: change forgotten password request
    //  and: user is found
    //  and: new password matching confirm password
    //  and: recovery token provided does not match recovery token on user
    // when: recovering password
    // then: business exception
    @Test(expected = BusinessException.class)
    public void testRecoverPasswordInvalidToken() {
        ChangeForgottenPasswordRequest request = new ChangeForgottenPasswordRequest("test", "1234", "test1", "test1");
        User user = getUser();
        when(userLookupService.lookupUserByUserName("test")).thenReturn(user);
        userController.recoverPassword(request, "0.0.0.0");
        verify(emailService, never()).sendEmail(anyString(), anyString(), anyString());
        verify(userLookupService, never()).updateUser(user, "0.0.0.0", true, "Password Recovery");
    }

    //given: change forgotten password request
    //  and: user is found
    //  and: new password matching confirm password
    //  and: user has no recovery token
    // when: recovering password
    // then: business exception
    @Test(expected = BusinessException.class)
    public void testRecoverPasswordUserNoRecoveryToken() {
        ChangeForgottenPasswordRequest request = new ChangeForgottenPasswordRequest("test", "1234", "test1", "test1");
        User user = getUser();
        user.setPasswordRecovery(null);
        when(userLookupService.lookupUserByUserName("test")).thenReturn(user);
        userController.recoverPassword(request, "0.0.0.0");
        verify(emailService, never()).sendEmail(anyString(), anyString(), anyString());
        verify(userLookupService, never()).updateUser(user, "0.0.0.0", true, "Password Recovery");
    }

    //given: change forgotten password request
    //  and: user is not found
    //  and: new password matching confirm password
    //  and: recovery token provided does match recovery token on user
    // when: recovering password
    // then: business exception
    @Test(expected = BusinessException.class)
    public void testRecoverPasswordNoUser() {
        ChangeForgottenPasswordRequest request = new ChangeForgottenPasswordRequest("test", "123", "test1", "test1");
        User user = null;
        when(userLookupService.lookupUserByUserName("test")).thenReturn(user);
        userController.recoverPassword(request, "0.0.0.0");
        verify(emailService, never()).sendEmail(anyString(), anyString(), anyString());
        verify(userLookupService, never()).updateUser(user, "0.0.0.0", true, "Password Recovery");
    }

    @Test
    public void testAddCameraListenerUserFound() {
        AddCameraEvent addCameraEvent = getAddCameraEvent();
        User user = getUser();
        when(userLookupService.lookUserById(1L)).thenReturn(user);
        userController.addCameraListener(addCameraEvent);
        verify(cameraEventService, times(1)).addCamera(addCameraEvent, user);
    }

    @Test
    public void testAddCameraListenerNoUserFound() {
        AddCameraEvent addCameraEvent = getAddCameraEvent();
        User user = null;
        when(userLookupService.lookUserById(1L)).thenReturn(user);
        userController.addCameraListener(addCameraEvent);
        verify(cameraEventService, never()).addCamera(addCameraEvent, user);
    }

    @Test
    public void testDeleteCameraEventUserFound() {
        DeleteCameraEvent dce = getDeleteCameraEvent();
        User user = getUser();
        when(userLookupService.lookUserById(1L)).thenReturn(user);
        userController.deleteCameraListener(dce);
        verify(cameraEventService, times(1)).deleteCamera(dce, user);
    }

    @Test
    public void testDeleteCameraEventNoUserFound() {
        DeleteCameraEvent dce = getDeleteCameraEvent();
        User user = null;
        when(userLookupService.lookUserById(1L)).thenReturn(user);
        userController.deleteCameraListener(dce);
        verify(cameraEventService, never()).deleteCamera(dce, user);
    }

    @Test
    public void testUpdateCameraEventUserFound() {
        UpdateCameraEvent uce = getUpdateCameraEvent();
        User user = getUser();
        when(userLookupService.lookUserById(1L)).thenReturn(user);
        userController.updateCameraListener(uce);
        verify(cameraEventService, times(1)).updateCamera(uce, user);
    }

    @Test
    public void testUpdateCameraEventNoUserFound() {
        UpdateCameraEvent uce = getUpdateCameraEvent();
        User user = null;
        when(userLookupService.lookUserById(1L)).thenReturn(user);
        userController.updateCameraListener(uce);
        verify(cameraEventService, never()).updateCamera(uce, user);
    }

    private User getUser() {
        return CommonTestEntityUtils.getUser();
    }

    private AddCameraEvent getAddCameraEvent() {
        AddCameraEvent addCameraEvent = new AddCameraEvent();
        addCameraEvent.setCameraName("cameraname");
        addCameraEvent.setUserId(1L);
        return addCameraEvent;
    }

    private DeleteCameraEvent getDeleteCameraEvent() {
        DeleteCameraEvent dce = new DeleteCameraEvent("test", 1L);
        return dce;
    }

    private UpdateCameraEvent getUpdateCameraEvent() {
        UpdateCameraEvent uce = new UpdateCameraEvent();
        uce.setCameraName("test");
        uce.setUserId(1L);
        return uce;
    }
}
