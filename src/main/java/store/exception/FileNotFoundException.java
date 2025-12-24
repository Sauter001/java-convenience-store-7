package store.exception;

public class FileNotFoundException extends ServiceException {
    public FileNotFoundException(String fileName) {
        super("File not found: " + fileName);
    }
}
