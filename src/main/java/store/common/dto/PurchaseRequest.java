package store.common.dto;

import java.util.Map;
import java.util.Set;

public record PurchaseRequest(
        Map<String, Integer> items
) {
    public static PurchaseRequest from(Map<String, Integer> cart) {
        return new PurchaseRequest(cart);
    }

    public PurchaseProductNames getProductNames() {
        return PurchaseProductNames.from(items.keySet());
    }

    public record PurchaseProductNames(
            Set<String> productNames
    ) {
        public static PurchaseProductNames from(Set<String> productNames) {
            return new PurchaseProductNames(productNames);
        }
    }
}
