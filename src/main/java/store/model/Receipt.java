package store.model;

import java.util.List;
import store.common.dto.PromotionResult;

public class Receipt {

    private final Cart cart;
    private final List<PromotionResult> promotionResult;
    private final int totalPrice;
    private final int promotionDiscount;
    private final int membershipDiscount;
    private final int paymentPrice;

    private Receipt(
            Cart cart,
            List<PromotionResult> promotionResult,
            int totalPrice,
            int promotionDiscount,
            int membershipDiscount,
            int paymentPrice
    ) {
        this.cart = cart;
        this.promotionResult = promotionResult;
        this.totalPrice = totalPrice;
        this.promotionDiscount = promotionDiscount;
        this.membershipDiscount = membershipDiscount;
        this.paymentPrice = paymentPrice;
    }

    public static Receipt of(Cart cart, List<PromotionResult> promotionResult, int membershipDiscount) {
        int totalPrice = cart.cart().entrySet().stream()
                .mapToInt(entry -> entry.getKey().price() * entry.getValue())
                .sum();
        int promotionDiscount = promotionResult.stream()
                .mapToInt(PromotionResult::discountAmount)
                .sum();
        return new Receipt(cart, promotionResult, totalPrice, promotionDiscount, membershipDiscount,
                totalPrice - promotionDiscount - membershipDiscount);
    }
}
