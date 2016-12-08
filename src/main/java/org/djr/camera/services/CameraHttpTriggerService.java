package org.djr.camera.services;

import org.apache.commons.codec.binary.Base64;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by djr4488 on 12/7/16.
 */
@ApplicationScoped
public class CameraHttpTriggerService {
    public boolean doCameraTrigger(String cameraUserName, String cameraPassword) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://192.168.1.183/adm/http_trigger.cgi");
        Invocation.Builder invocation = target.request(MediaType.TEXT_HTML_TYPE)
                .header("Authorization", "Basic " + getAuthorizationToken(cameraUserName, cameraPassword));
        Response response = invocation.get();
        int status = response.getStatus();
        if (status >= 200 && status < 300) {
            return true;
        } else {
            return false;
        }
    }

    public String getAuthorizationToken(String cameraUserName, String cameraPassword) {
        byte[] bytes = (cameraUserName + ":" + cameraPassword).getBytes();
        return Base64.encodeBase64String(bytes);
    }
}
