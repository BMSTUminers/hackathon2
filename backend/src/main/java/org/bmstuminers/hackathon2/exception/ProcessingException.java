package org.bmstuminers.hackathon2.exception;

/**
 * Exception raised in case of error during chain processing
 * @author Konstantin Grechishchev
 */
public class ProcessingException extends BaseException {

    public ProcessingException() {
        super();
    }

    public ProcessingException(String message) {
        super(message);
    }

    public ProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProcessingException(Throwable cause) {
        super(cause);
    }
}
