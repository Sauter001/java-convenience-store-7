package store.error;

public class ProductNotExistsException extends StoreException {
    public ProductNotExistsException() {
        super("존재하지 않는 상품입니다. 다시 입력해 주세요.");
    }
}
