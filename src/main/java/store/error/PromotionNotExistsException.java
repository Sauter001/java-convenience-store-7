package store.error;

public class PromotionNotExistsException extends StoreException {
    public PromotionNotExistsException() {
        super("존재하지 않는 프로모션입니다. 다시 입력해 주세요.");
    }
}
