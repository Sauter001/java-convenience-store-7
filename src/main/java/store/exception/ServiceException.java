package store.exception;

public class ServiceException extends IllegalArgumentException {
    public ServiceException(ErrorMessage errorMessage) {
        super(getErrorMessage(errorMessage));
    }

    private static String getErrorMessage(ErrorMessage errorMessage) {
        String errorPrefix = "[ERROR] ";
        return errorPrefix + errorMessage.getMessage();
    }
}
