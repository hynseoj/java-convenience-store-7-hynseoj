package store.model;

import java.util.HashMap;
import java.util.Map;

public class Cart {

    private final Map<Product, Integer> cart;

    private Cart(Map<Product, Integer> cart) {
        this.cart = cart;
    }

    public static Cart empty() {
        return new Cart(new HashMap<>());
    }

    public void addProduct(Product product, int quantity) {
        cart.merge(product, quantity, Integer::sum);
    }

    public Map<Product, Integer> cart() {
        return cart;
    }
}
