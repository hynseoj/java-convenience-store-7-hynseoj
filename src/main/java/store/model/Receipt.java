package store.model;

import java.util.List;
import store.common.dto.PromotionResult;

public class Receipt {

    private final Cart cart;
    private final List<PromotionResult> promotionResults;
    private final int totalPrice;
    private final int promotionDiscount;
    private final int membershipDiscount;
    private final int paymentPrice;

    private Receipt(
            Cart cart,
            List<PromotionResult> promotionResults,
            int totalPrice,
            int promotionDiscount,
            int membershipDiscount,
            int paymentPrice
    ) {
        this.cart = cart;
        this.promotionResults = promotionResults;
        this.totalPrice = totalPrice;
        this.promotionDiscount = promotionDiscount;
        this.membershipDiscount = membershipDiscount;
        this.paymentPrice = paymentPrice;
    }

    public static Receipt of(Cart cart, List<PromotionResult> promotionResults, int membershipDiscount) {
        int totalPrice = cart.cart().entrySet().stream()
                .mapToInt(entry -> entry.getKey().price() * entry.getValue())
                .sum();
        int promotionDiscount = promotionResults.stream()
                .mapToInt(PromotionResult::discountAmount)
                .sum();
        return new Receipt(cart, promotionResults, totalPrice, promotionDiscount, membershipDiscount,
                totalPrice - promotionDiscount - membershipDiscount);
    }

    public Cart cart() {
        return cart;
    }

    public List<PromotionResult> promotionResults() {
        return promotionResults;
    }

    public int totalPrice() {
        return totalPrice;
    }

    public int promotionDiscount() {
        return promotionDiscount;
    }

    public int membershipDiscount() {
        return membershipDiscount;
    }

    public int paymentPrice() {
        return paymentPrice;
    }
}
