package kz.ezdrav.eps_smartbridge_adapter.exception;

/**
 * Exception thrown when an error occurs during EPS service processing.
 */
public class EpsServiceException extends RuntimeException {

    public EpsServiceException(String message) {
        super(message);
    }

    public EpsServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public EpsServiceException(Throwable cause) {
        super(cause);
    }
}
