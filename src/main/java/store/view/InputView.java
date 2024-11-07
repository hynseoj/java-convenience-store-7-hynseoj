package store.view;

import static store.common.constant.ErrorMessage.INPUT_EMPTY_ERROR;

import camp.nextstep.edu.missionutils.Console;

public class InputView {

    public String getPurchaseItems() {
        System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
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
