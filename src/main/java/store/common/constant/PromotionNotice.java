package store.common.constant;

import java.util.regex.Pattern;

public enum PromotionNotice {
    DEFAULT_NOTICE(""),
    GET_FREE_M_NOTICE("현재 %s은(는) %d개를 무료로 더 받을 수 있습니다."),
    OUT_OF_STOCK_NOTICE("현재 %s %d개는 프로모션 할인이 적용되지 않습니다.");

    private final String message;

    PromotionNotice(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }

    public String message(String content1, int content2) {
        return String.format(message, content1, content2);
    }

    public boolean matches(String formattedMessage) {
        String regex = this.message
                .replace("%s", ".+")
                .replace("%d", "\\d+")
                .replace("(", "\\(")
                .replace(")", "\\)");
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(formattedMessage).matches();
    }
}
