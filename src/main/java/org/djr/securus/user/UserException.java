package org.djr.securus.user;

import org.djr.securus.exceptions.BusinessException;

/**
 * Created by djr4488 on 12/10/16.
 */
public class UserException extends BusinessException {
    public UserException() {
        super();
    }

    public UserException(String message) {
        super(message);
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserException(Throwable cause) {
        super(cause);
    }

    protected UserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
