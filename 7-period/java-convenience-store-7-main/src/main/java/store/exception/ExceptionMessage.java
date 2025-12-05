package store.exception;

public enum ExceptionMessage {

    INPUT_BLANK("빈 값을 입력하셨습니다."),
    PROMOTION_NOT_FOUND("존재하지 않는 프로모션입니다."),
    INVALID_PRODUCT_QUANTITY_FORMAT("잘못된 상품 및 수량 입력 형식입니다."),
    PRODUCT_NOT_FOUND("존재하지 않는 상품입니다."),
    INVALID_PURCHASED_QUANTITY("최소 1개 이상 구매 가능합니다."),
    ;
    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
