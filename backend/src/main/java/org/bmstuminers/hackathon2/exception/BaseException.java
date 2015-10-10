package org.bmstuminers.hackathon2.exception;

/**
 * General project exception
 * @author Konstantin Grechishchev
 */
public class BaseException extends Exception {

    public BaseException() {
        super();
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }
}
