package org.djr.camera.messaging.email;

import org.aeonbits.owner.Config;

/**
 * Created by djr4488 on 11/22/16.
 */
@Config.Sources({
        "classpath:EmailConfig.properties"
})
public interface EmailConfig extends Config {
    String destination();
    String from();
}
