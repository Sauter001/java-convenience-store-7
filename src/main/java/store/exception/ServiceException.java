package store.exception;

public class ServiceException extends IllegalArgumentException {
    public ServiceException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
    }
}
