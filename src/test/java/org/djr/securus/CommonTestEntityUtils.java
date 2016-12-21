package org.djr.securus;

import org.apache.commons.codec.binary.Base64;
import org.djr.securus.camera.rest.trigger.TriggerEvent;
import org.djr.securus.entities.Camera;
import org.djr.securus.entities.User;
import org.djr.securus.user.PasswordUtils;

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
}
