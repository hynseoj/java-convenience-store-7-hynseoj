package store.model;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import store.common.dto.PromotionConditionResult;
import store.common.dto.PromotionResult;

public class Product {

    private final String name;
    private final int price;
    private final AtomicInteger stock;
    private final Promotion promotion;

    private Product(String name, int price, int stock, Promotion promotion) {
        this.name = name;
        this.price = price;
        this.stock = new AtomicInteger(stock);
        this.promotion = promotion;
    }

    public static Product of(String name, int price, int stock, Promotion promotion) {
        return new Product(name, price, stock, promotion);
    }

    public boolean isPromotionApplicable() {
        if (promotion == null) {
            return false;
        }
        return promotion.isWithinPromotionPeriod();
    }

    public PromotionResult applyPromotion(int quantity) {
        return promotion.applyPromotion(this, quantity);
    }

    public PromotionConditionResult checkPromotionCondition(int quantity) {
        return promotion.checkCondition(this, quantity);
    }

    public boolean isInStock(int quantity) {
        return stock.get() >= quantity;
    }

    public void reduceStock(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException();
        }
        stock.addAndGet(-quantity);
    }

    public String name() {
        return name;
    }

    public int price() {
        return price;
    }

    public int stock() {
        return stock.get();
    }

    public Promotion promotion() {
        return promotion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Product product = (Product) o;
        return Objects.equals(name, product.name) && Objects.equals(promotion, product.promotion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, promotion);
    }
}
