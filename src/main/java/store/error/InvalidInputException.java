package store.error;

public class InvalidInputException extends StoreException {
    public InvalidInputException() {
        super("잘못된 입력입니다. 다시 입력해 주세요.");
    }
}
