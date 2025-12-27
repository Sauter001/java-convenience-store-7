package store.exception;

public class DomainNotFoundException extends ServiceException {
    public DomainNotFoundException(String fileName) {
        super("File for domain not found: " + fileName);
    }
}
