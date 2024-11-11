package store.model;

import java.util.Objects;
import java.util.Set;

public class Products {

    private final Set<Product> products;

    public Products(Set<Product> products) {
        this.products = products;
    }

    public static Products from(Set<Product> products) {
        return new Products(products);
    }

    public Set<Product> products() {
        return products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Products products = (Products) o;
        return Objects.equals(this.products, products.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(products);
    }
}
