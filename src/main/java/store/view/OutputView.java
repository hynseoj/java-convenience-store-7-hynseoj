package store.view;

import java.text.NumberFormat;
import java.util.Map;
import java.util.stream.Collectors;
import store.model.Product;
import store.model.ProductCatalog;
import store.model.Promotion;
import store.model.Receipt;

public class OutputView {

    private static final String INTRO_MESSAGE = "안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.";
    private static final String PRODUCT_MESSAGE = "- %s %s원 %s %s\n";
    private static final String OUT_OF_STOCK_MESSAGE = "재고 없음";

    public void printStoreInventory(ProductCatalog productCatalog) {
        System.out.println(INTRO_MESSAGE);
        productCatalog.products().forEach(this::printProduct);
        System.out.println();
    }

    public void printGetFreeNotice(String message) {
        System.out.print(message + "추가하시겠습니까?");
    }

    public void printPromotionOutOfStockNotice(String message) {
        System.out.print(message + "그래도 구매하시겠습니까?");
    }

    public void printMembershipNotice() {
        System.out.print("멤버십 할인을 받으시겠습니까?");
    }

    public void printReceipt(Receipt receipt) {
        StringBuilder receiptBuilder = new StringBuilder();

        receiptBuilder.append("================W 편의점================\n");
        Map<String, Integer> groupedItems = receipt.cart().cart().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().name(),
                        Map.Entry::getValue,
                        Integer::sum
                ));
        receiptBuilder.append(String.format("%-10s %5s %10s\n", "상품명", "수량", "금액"));
        groupedItems.forEach((productName, quantity) -> {
            int price = receipt.cart().cart().entrySet().stream()
                    .filter(entry -> entry.getKey().name().equals(productName))
                    .mapToInt(entry -> entry.getKey().price() * entry.getValue())
                    .sum();
            if (quantity > 0) {
                receiptBuilder.append(String.format("%-10s %5d %,10d\n", productName, quantity, price));
            }
        });
        receiptBuilder.append("===============증 정==================\n");
        receipt.promotionResults().forEach(result -> {
            if (result.freeQuantity() > 0) {
                receiptBuilder.append(String.format("%-10s %5d\n", result.productName(), result.freeQuantity()));
            }
        });
        receiptBuilder.append("=====================================\n");
        receiptBuilder.append(String.format("%-10s %5d %,10d\n", "총구매액",
                groupedItems.values().stream().mapToInt(Integer::intValue).sum(), receipt.totalPrice()));
        receiptBuilder.append(String.format("%-10s %,15d\n", "행사할인", -receipt.promotionDiscount()));
        receiptBuilder.append(String.format("%-10s %,15d\n", "멤버십할인", -receipt.membershipDiscount()));
        receiptBuilder.append(String.format("%-10s %,15d\n", "내실돈", receipt.paymentPrice()));

        System.out.println(receiptBuilder);
    }

    public void printRepurchaseNotice() {
        System.out.print("감사합니다. 구매하고 싶은 다른 상품이 있나요?");
    }

    private void printProduct(Product product) {
        NumberFormat formatter = NumberFormat.getInstance();

        System.out.printf(
                PRODUCT_MESSAGE,
                product.name(),
                formatter.format(product.price()),
                getStockValue(product.stock()),
                getPromotionNameValue(product.promotion())
        );
    }

    private String getStockValue(int stock) {
        if (stock == 0) {
            return OUT_OF_STOCK_MESSAGE;
        }
        return String.valueOf(stock) + "개";
    }

    private String getPromotionNameValue(Promotion promotion) {
        String promotionName = "";
        if (promotion != null) {
            promotionName = promotion.name();
        }
        return promotionName;
    }
}
