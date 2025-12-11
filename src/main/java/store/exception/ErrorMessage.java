package store.exception;

public enum ErrorMessage {
    INVALID_INPUT_FORMAT("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요."),
    PRODUCT_NOT_EXIST("존재하지 않는 상품 입니다. 다시 입력해 주세요."),
    SOLD_OUT("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요."),
    FILE_READ_FAIL("파일 조회 실패");
    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
