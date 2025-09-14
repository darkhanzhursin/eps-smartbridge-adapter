package kz.ezdrav.eps_smartbridge_adapter.exception;

/**
 * Exception thrown when an error occurs during service processing.
 */
public class ServiceProcessingException extends EpsServiceException {

    public ServiceProcessingException(String message) {
        super(message);
    }

    public ServiceProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceProcessingException(Throwable cause) {
        super(cause);
    }
}
