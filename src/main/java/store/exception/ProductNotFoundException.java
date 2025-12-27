package store.exception;

public class ProductNotFoundException extends ServiceException{
    public ProductNotFoundException() {
        super("존재하지 않는 상품입니다. 다시 입력해주세요.");
    }
}
