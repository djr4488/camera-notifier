package org.djr.securus.user.rest.add;

import org.djr.securus.user.UserException;

/**
 * Created by djr4488 on 12/10/16.
 */
public class UserExistsException extends UserException {
    public UserExistsException() {
        super();
    }

    public UserExistsException(String message) {
        super(message);
    }

    public UserExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserExistsException(Throwable cause) {
        super(cause);
    }

    protected UserExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
