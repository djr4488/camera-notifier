package org.djr.securus;

import org.apache.commons.codec.binary.Base64;
import org.djr.securus.camera.rest.trigger.TriggerEvent;
import org.djr.securus.entities.Camera;
import org.djr.securus.entities.Token;
import org.djr.securus.entities.User;
import org.djr.securus.user.PasswordUtils;
import org.djr.securus.user.rest.add.AddUserRequest;
import org.djr.securus.user.rest.login.LoginRequest;
import org.djr.securus.user.rest.password.ChangeForgottenPasswordRequest;
import org.djr.securus.user.rest.password.ChangePasswordRequest;
import org.djr.securus.user.rest.password.InitForgotPasswordRequest;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;

/**
 * Created by djr4488 on 12/19/16.
 */
public class CommonTestEntityUtils {
    public static User getUser() {
        byte[] userSaltByteArray = PasswordUtils.getNextSalt();
        String userSalt = Base64.encodeBase64String(userSaltByteArray);
        String userPassword = Base64.encodeBase64String(PasswordUtils.hash("test".toCharArray(), userSaltByteArray));
        User user = new User();
        user.setUserName("test");
        user.setPassword(userPassword);
        user.setSalt(userSalt);
        user.setEmailAddress("test@test.com");
        user.setPasswordRecovery("123");
        return user;
    }

    public static Camera getCamera(String name, String zoneName) {
        Camera camera = new Camera();
        camera.setCameraName(name);
        camera.setCameraZone(zoneName);
        camera.setCameraTriggerUrl("triggerUrl");
        camera.setCameraAdministrator("admin");
        camera.setCameraPassword("password");
        camera.setProcessPostEvents(true);
        camera.setProcessNotifyEvents(true);
        return camera;
    }

    public static List<Camera> getCameraList() {
        List<Camera> cameraList = new ArrayList<>();
        cameraList.add(getCamera("name1", "zoneName1"));
        cameraList.add(getCamera("name2", "zoneName2"));
        return cameraList;
    }

    public static TriggerEvent getTriggerEvent() {
        TriggerEvent triggerEvent = new TriggerEvent(1L, "zoneName1");
        return triggerEvent;
    }

    public static CameraPostEvent getCameraPostEvent() {
        CameraPostEvent cameraPostEvent = new CameraPostEvent();
        cameraPostEvent.setCameraName("name");
        cameraPostEvent.setUserName("userName");
        return cameraPostEvent;
    }

    public static CameraNotifyEvent getCameraNotifyEvent() {
        CameraNotifyEvent cameraNotifyEvent = new CameraNotifyEvent("name", "0.0.0.0", "auth", "user", "password");
        return cameraNotifyEvent;
    }

    public static AddUserRequest getAddUserRequest() {
        AddUserRequest addUserRequest = new AddUserRequest("user", "password", "password", "test@test.com");
        return addUserRequest;
    }

    public static LoginRequest getLoginRequest() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserName("user");
        loginRequest.setPassword("password");
        return loginRequest;
    }

    public static Token getToken(User user) {
        DateTime dateTime = DateTime.now().withYear(2016).withMonthOfYear(DateTimeConstants.JANUARY)
                .withDayOfMonth(1).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0)
                .withMillisOfSecond(0);
        return new Token("token", dateTime.toDate(), user);
    }

    public static ChangePasswordRequest getChangePasswordRequest() {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setOldPassword("old");
        request.setNewPassword("new");
        request.setConfirmPassword("new");
        return request;
    }

    public static InitForgotPasswordRequest getInitForgotPasswordRequest() {
        InitForgotPasswordRequest request = new InitForgotPasswordRequest("test");
        return request;
    }

    public static ChangeForgottenPasswordRequest getChangeForgottenPasswordRequest() {
        ChangeForgottenPasswordRequest request = new ChangeForgottenPasswordRequest("test", "token", "new", "new");
        return request;
    }
}
