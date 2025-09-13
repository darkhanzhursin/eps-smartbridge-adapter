package kz.ezdrav.eps_smartbridge_adapter.exception;

public abstract class GeneralException extends RuntimeException {
    public final String message;

    public GeneralException(String message) {
        super(message);
        this.message = message;
    }
}