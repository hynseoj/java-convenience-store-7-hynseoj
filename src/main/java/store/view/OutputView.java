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
    private static final String STOCK_COUNT_UNIT = "개";
    private static final String OUT_OF_STOCK_MESSAGE = "재고 없음";
    private static final String ASK_TO_ADD_MESSAGE = "추가하시겠습니까?";
    private static final String ASK_TO_PURCHASE_MESSAGE = "그래도 구매하시겠습니까?";
    private static final String ASK_TO_MEMBERSHIP_MESSAGE = "멤버십 할인을 받으시겠습니까?";
    private static final String ASK_TO_REPURCHASE_MESSAGE = "감사합니다. 구매하고 싶은 다른 상품이 있나요?";

    public void printStoreInventory(ProductCatalog productCatalog) {
        System.out.println(INTRO_MESSAGE);
        productCatalog.products().forEach(this::printProduct);
        System.out.println();
    }

    public void printGetFreeNotice(String message) {
        System.out.print(message + ASK_TO_ADD_MESSAGE);
    }

    public void printPromotionOutOfStockNotice(String message) {
        System.out.print(message + ASK_TO_PURCHASE_MESSAGE);
    }

    public void printMembershipNotice() {
        System.out.print(ASK_TO_MEMBERSHIP_MESSAGE);
    }

    public void printReceipt(Receipt receipt) {
        StringBuilder receiptBuilder = new StringBuilder();

        receiptBuilder.append("================W 편의점================\n");
        receiptBuilder.append(String.format("%-10s %5s %10s\n", "상품명", "수량", "금액"));
        printPurchaseItems(receipt, receiptBuilder);
        receiptBuilder.append("=================증 정=================\n");
        printPromotionItems(receipt, receiptBuilder);
        receiptBuilder.append("=====================================\n");
        printSummary(receipt, receiptBuilder);

        System.out.println(receiptBuilder);
    }

    private void printSummary(Receipt receipt, StringBuilder receiptBuilder) {
        Map<String, Integer> groupedItems = makeGroupByProductName(receipt);
        receiptBuilder.append(String.format("%-12s %5d %,12d\n", "총구매액",
                groupedItems.values().stream().mapToInt(Integer::intValue).sum(), receipt.totalPrice()));
        receiptBuilder.append(String.format("%-15s %,15d\n", "행사할인", -receipt.promotionDiscount()));
        receiptBuilder.append(String.format("%-15s %,15d\n", "멤버십할인", -receipt.membershipDiscount()));
        receiptBuilder.append(String.format("%-15s %,15d\n", "내실돈", receipt.paymentPrice()));
    }

    private void printPromotionItems(Receipt receipt, StringBuilder receiptBuilder) {
        receipt.promotionResults().forEach(result -> {
            if (result.freeQuantity() > 0) {
                receiptBuilder.append(String.format("%-12s %5d\n", result.productName(), result.freeQuantity()));
            }
        });
    }

    private void printPurchaseItems(Receipt receipt, StringBuilder receiptBuilder) {
        Map<String, Integer> groupedItems = makeGroupByProductName(receipt);
        groupedItems.forEach((productName, quantity) -> {
            int price = receipt.cart().cart().entrySet().stream()
                    .filter(entry -> entry.getKey().name().equals(productName))
                    .mapToInt(entry -> entry.getKey().price() * entry.getValue())
                    .sum();
            if (quantity > 0) {
                receiptBuilder.append(String.format("%-12s %5d %,12d\n", productName, quantity, price));
            }
        });
    }

    private Map<String, Integer> makeGroupByProductName(Receipt receipt) {
        return receipt.cart().cart().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().name(),
                        Map.Entry::getValue,
                        Integer::sum
                ));
    }

    public void printRepurchaseNotice() {
        System.out.print(ASK_TO_REPURCHASE_MESSAGE);
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
        return stock + STOCK_COUNT_UNIT;
    }

    private String getPromotionNameValue(Promotion promotion) {
        String promotionName = "";
        if (promotion != null) {
            promotionName = promotion.name();
        }
        return promotionName;
    }
}
