package store.model;

import java.util.Set;
import java.util.stream.Collectors;

public class Products {

    private final Set<Product> products;

    private Products(Set<Product> products) {
        this.products = products;
    }

    public static Products from(Set<Product> products) {
        Set<Product> additionalProducts = products.stream()
                .collect(Collectors.groupingBy(Product::name))
                .entrySet().stream()
                .filter(entry -> entry.getValue().stream().noneMatch(p -> p.promotion() == null))
                .map(entry -> Product.of(entry.getKey(), entry.getValue().getFirst().price(), 0, null))
                .collect(Collectors.toSet());

        products.addAll(additionalProducts);
        return new Products(products);
    }

    public boolean doesContainsAllProduct(Set<String> products) {
        return this.products.stream()
                .map(Product::name)
                .collect(Collectors.toSet())
                .containsAll(products);
    }

    public Set<Product> products() {
        return products;
    }
}
