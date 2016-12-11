package org.djr.securus;

import com.djr4488.metrics.rest.MetricsApi;
import org.djr.securus.camera.rest.notify.CameraNotifyInitiator;
import org.djr.securus.camera.rest.post.CameraPostInitiator;
import org.djr.securus.user.rest.add.AddUserInitiator;
import org.djr.securus.camera.rest.trigger.TriggerInitiator;
import org.djr.securus.camera.rest.management.UserCameraInitiator;
import org.djr.securus.user.rest.login.LoginInitiator;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by djr4488 on 11/22/16.
 */
@ApplicationPath("api")
public class SecurusApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        return new HashSet<>(Arrays.asList(MetricsApi.class, CameraNotifyInitiator.class, CameraPostInitiator.class,
                AddUserInitiator.class, TriggerInitiator.class, LoginInitiator.class, UserCameraInitiator.class));
    }
}
