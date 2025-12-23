package store.exception;

public class ServiceException extends IllegalArgumentException {
    public static final String ERROR_PREFIX = "[ERROR] ";

    public ServiceException(String message) {
        super(ERROR_PREFIX + message);
    }
}
