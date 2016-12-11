package org.djr.securus.camera.rest;

import org.apache.commons.codec.binary.Base64;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by djr4488 on 12/11/16.
 */
public class CameraAuthenticationUtils {
    public static Map<String, String> getUserNameAndPasswordAsMap(String base64EncodedAuthorization) {
        String[] userNameAndPasswordDecoded =
                new String(Base64.decodeBase64(base64EncodedAuthorization.substring(6))).split(":");
        Map<String, String> userNameAndPasswordMap = new HashMap<>();
        userNameAndPasswordMap.put("userName", userNameAndPasswordDecoded[0]);
        userNameAndPasswordMap.put("password", userNameAndPasswordDecoded[1]);
        return userNameAndPasswordMap;
    }
}
