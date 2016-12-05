package org.djr.camera;

import com.djr4488.metrics.rest.MetricsApi;
import org.djr.camera.notify.CameraNotifyInitiator;
import org.djr.camera.post.CameraPostInitiator;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by djr4488 on 11/22/16.
 */
@ApplicationPath("api")
public class CameraApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        return new HashSet<Class<?>>(Arrays.asList(CameraNotifyInitiator.class, CameraPostInitiator.class,
                MetricsApi.class));
    }
}
