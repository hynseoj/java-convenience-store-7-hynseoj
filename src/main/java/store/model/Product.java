package store.model;

import java.util.Objects;

public class Product {

    private final String name;
    private final int price;
    private final int stock;
    private final Promotion promotion;

    private Product(String name, int price, int stock, Promotion promotion) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.promotion = promotion;
    }

    public static Product of(String name, int price, int stock, Promotion promotion) {
        return new Product(name, price, stock, promotion);
    }

    public boolean isPromotionApplicable() {
        return promotion.isWithinPromotionPeriod();
    }

    public String name() {
        return name;
    }

    public int price() {
        return price;
    }

    public int stock() {
        return stock;
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
