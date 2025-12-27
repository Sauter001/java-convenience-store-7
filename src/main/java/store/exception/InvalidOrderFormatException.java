package store.exception;

public class InvalidOrderFormatException extends ServiceException {
    public InvalidOrderFormatException() {
        super("올바르지 않은 형식으로 입력했습니다. 다시 입력해주세요.");
    }
}
