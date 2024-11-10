package store.model;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ProductCatalog {

    private final Products products;

    private ProductCatalog(Products products) {
        this.products = products;
    }

    public static ProductCatalog from(Set<Product> products) {
        products = new HashSet<>(products);
        Set<Product> additionalProducts = products.stream()
                .collect(Collectors.groupingBy(Product::name))
                .entrySet().stream()
                .filter(entry -> entry.getValue().stream().noneMatch(p -> p.promotion() == null))
                .map(entry -> Product.of(entry.getKey(), entry.getValue().getFirst().price(), 0, null))
                .collect(Collectors.toSet());

        products.addAll(additionalProducts);
        return new ProductCatalog(Products.from(products));
    }

    public boolean doesContainsAllProduct(Set<String> productNames) {
        return this.products.products().stream()
                .map(Product::name)
                .collect(Collectors.toSet())
                .containsAll(productNames);
    }

    public Products getProductByName(String productName) {
        return Products.from(
                this.products.products().stream()
                        .filter(product -> product.name().equals(productName))
                        .collect(Collectors.toSet())
        );
    }

    public Products getProductsByNames(Set<String> productNames) {
        Set<Product> productsByName = new HashSet<>();
        productNames.forEach(productName ->
                productsByName.addAll(products.products().stream()
                        .filter(product -> product.name().equals(productName))
                        .collect(Collectors.toSet()))
        );
        return Products.from(productsByName);
    }

    public Set<Product> products() {
        return products.products();
    }
}
