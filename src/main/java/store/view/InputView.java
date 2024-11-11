package store.view;

import static store.common.constant.ErrorMessage.INPUT_EMPTY_ERROR;

import camp.nextstep.edu.missionutils.Console;

public class InputView {

    private static final String PURCHASE_ITEMS_REQUEST_PROMPT = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    private static final String YES_OR_NO_PROMPT = " (Y/N)";

    public String getPurchaseItems() {
        System.out.println(PURCHASE_ITEMS_REQUEST_PROMPT);
        String message = Console.readLine().strip();
        validateNotEmpty(message);
        return message;
    }

    public String getYesOrNo() {
        System.out.println(YES_OR_NO_PROMPT);
        String message = Console.readLine().strip();
        validateNotEmpty(message);
        return message;
    }

    private void validateNotEmpty(String message) {
        if (message == null || message.isEmpty()) {
            throw new IllegalArgumentException(INPUT_EMPTY_ERROR.message());
        }
    }
}
