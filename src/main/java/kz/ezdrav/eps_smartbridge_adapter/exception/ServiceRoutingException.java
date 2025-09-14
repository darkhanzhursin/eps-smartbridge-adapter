package kz.ezdrav.eps_smartbridge_adapter.exception;

/**
 * Exception thrown when an error occurs during service routing.
 */
public class ServiceRoutingException extends EpsServiceException {

    public ServiceRoutingException(String message) {
        super(message);
    }

    public ServiceRoutingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceRoutingException(Throwable cause) {
        super(cause);
    }
}
