package org.djr.securus.user.rest;

import org.djr.securus.exceptions.BusinessException;

/**
 * Created by djr4488 on 12/10/16.
 */
public class UserAuthorizationFilterException extends BusinessException {
    public UserAuthorizationFilterException() {
        super();
    }

    public UserAuthorizationFilterException(String message) {
        super(message);
    }

    public UserAuthorizationFilterException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserAuthorizationFilterException(Throwable cause) {
        super(cause);
    }

    protected UserAuthorizationFilterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
