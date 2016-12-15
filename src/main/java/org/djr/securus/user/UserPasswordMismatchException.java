package org.djr.securus.user;

import org.djr.securus.exceptions.BusinessException;

/**
 * Created by djr4488 on 12/14/16.
 */
public class UserPasswordMismatchException extends BusinessException {
    public UserPasswordMismatchException() {
        super();
    }

    public UserPasswordMismatchException(String message) {
        super(message);
    }

    public UserPasswordMismatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserPasswordMismatchException(Throwable cause) {
        super(cause);
    }

    protected UserPasswordMismatchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
