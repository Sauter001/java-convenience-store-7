package store.exception;

public class ServiceException extends IllegalArgumentException {
    public ServiceException(String message) {
        super(message);
    }
}
