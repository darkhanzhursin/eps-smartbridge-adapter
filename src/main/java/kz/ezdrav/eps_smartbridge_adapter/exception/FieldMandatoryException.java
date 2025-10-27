package kz.ezdrav.eps_smartbridge_adapter.exception;

public class FieldMandatoryException extends EpsServiceException {
    public FieldMandatoryException(String message) {
        super(message);
    }

    public FieldMandatoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public FieldMandatoryException(Throwable cause) {
        super(cause);
    }
}
