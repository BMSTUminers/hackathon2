package org.bmstuminers.hackathon2.exception;

/**
 * Exception raised when wrong parameter list is passed into Block
 * @author Konstantin Grechishchev
 */
public class WrongParameterException extends BaseException {

    public WrongParameterException() {
        super();
    }

    public WrongParameterException(String message) {
        super(message);
    }

    public WrongParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongParameterException(Throwable cause) {
        super(cause);
    }

}
