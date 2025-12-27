package store.exception;

public class InvalidInputException extends ServiceException {
    public InvalidInputException() {
        super("잘못된 입력입니다. 다시 입력해 주세요.");
    }
}
