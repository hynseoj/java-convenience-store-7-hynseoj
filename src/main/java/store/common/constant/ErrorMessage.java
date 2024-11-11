package store.common.constant;

public enum ErrorMessage {
    INPUT_EMPTY_ERROR("잘못된 입력입니다. 다시 입력해 주세요."),
    INPUT_INVALID_FORMAT("올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요."),

    PRODUCT_NOT_FOUND_ERROR("존재하지 않는 상품입니다. 다시 입력해 주세요."),
    PRODUCT_OUT_OF_STOCK("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");

    private static final String ERROR_MESSAGE_PREFIX = "[ERROR] ";
    private final String message;

    ErrorMessage(String message) {
        this.message = ERROR_MESSAGE_PREFIX + message;
    }

    public String message() {
        return message;
    }
}
